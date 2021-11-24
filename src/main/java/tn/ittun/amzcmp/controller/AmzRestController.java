
package tn.ittun.amzcmp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.Availability;
import com.amazon.paapi5.v1.Condition;
import com.amazon.paapi5.v1.DeliveryFlag;
import com.amazon.paapi5.v1.ErrorData;
import com.amazon.paapi5.v1.GetItemsRequest;
import com.amazon.paapi5.v1.GetItemsResource;
import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.Images;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.PartnerType;
import com.amazon.paapi5.v1.SearchItemsRequest;
import com.amazon.paapi5.v1.SearchItemsResource;
import com.amazon.paapi5.v1.SearchItemsResponse;
import com.amazon.paapi5.v1.SortBy;
import com.amazon.paapi5.v1.api.DefaultApi;

import tn.ittun.amzcmp.entity.Account;
import tn.ittun.amzcmp.entity.BuyingInfo;
import tn.ittun.amzcmp.entity.Product;
import tn.ittun.amzcmp.entity.ProductSearchResult;
import tn.ittun.amzcmp.entity.SearchFilter;
import tn.ittun.amzcmp.entity.User;
import tn.ittun.amzcmp.helper.CurrencyUtils;
import tn.ittun.amzcmp.repository.AccountRepository;
import tn.ittun.amzcmp.repository.ProductRepository;
import tn.ittun.amzcmp.repository.UserRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AmzRestController
{
	@Autowired tn.ittun.amzcmp.service.ApiClientService	apiClientService;
	@Autowired UserRepository							userRepository;
	@Autowired ProductRepository						prodcutRepository;
	@Autowired AccountRepository						accountRepository;

	@PostMapping("/search")
	public ProductSearchResult search(@RequestBody SearchFilter searchFilter, @RequestParam(required = false) Long userId)
	{
		ApiClient client = apiClientService.getApiClientForLocale( searchFilter.getPrimaryCountry());
		DefaultApi api = new DefaultApi( client);
		Account account = accountRepository.loadAccountByLocaleCode( searchFilter.getPrimaryCountry());
		String partnerTag = account.getTag();

		List<SearchItemsResource> searchItemsResources = setResourcesToRequest();

		String searchIndex = "All";

		// Sending the request
		SearchItemsRequest searchItemsRequest = new SearchItemsRequest();
		searchItemsRequest.setItemPage( searchFilter.getItemPage());
		searchItemsRequest.partnerTag( partnerTag).searchIndex( searchIndex).resources( searchItemsResources)
			.partnerType( PartnerType.ASSOCIATES);
		// apply the search criteria
		setSearchCriteria( searchFilter, searchItemsRequest);

		try
		{
			// Forming the request
			SearchItemsResponse response = api.searchItems( searchItemsRequest);
			System.out.println( "API called successfully");
			if( response.getSearchResult() == null || response.getSearchResult().getItems() == null)
			{ return new ProductSearchResult(); }
			// System.out.println( "Complete response: " + response);
			ProductSearchResult productSearchResult = new ProductSearchResult();
			List<Product> productsList = new ArrayList<>();
			List<String> productsAsinsList = new ArrayList<>();
			productSearchResult.setResultCount( response.getSearchResult().getTotalResultCount());
			productSearchResult.setItemsCount( response.getSearchResult().getItems().size());
			// set isLastPage , by default get 10 items per request
			boolean hasMore = response.getSearchResult().getTotalResultCount()
				- (searchFilter.getItemPage() * response.getSearchResult().getItems().size()) > 0;
			productSearchResult.setHasMore( hasMore);
			try
			{
				saveInformationsToProduct( userId, response, productsList, productsAsinsList, searchFilter.getPrimaryCountry());
			}
			catch( NullPointerException e)
			{
				e.printStackTrace();
			}

			getPricesFromSecondaryAccounts( searchFilter, productsList, productsAsinsList);
			try
			{
				convertAndSortPrices( productsList, searchFilter.getCurrencyOfPreference());
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}

			productSearchResult.setProducts( productsList);

			if( response.getErrors() != null)
			{
				System.out.println( "Printing errors:\nPrinting Errors from list of Errors");
				for( ErrorData error : response.getErrors())
				{
					System.out.println( "Error code: " + error.getCode());
					System.out.println( "Error message: " + error.getMessage());
				}
			}
			return productSearchResult;
		}
		catch( ApiException exception)
		{
			System.out.println( "Error calling PA-API 5.0!");
			System.out.println( "Status code: " + exception.getCode());
			System.out.println( "Errors: " + exception.getResponseBody());
			System.out.println( "Message: " + exception.getMessage());
			if( exception.getResponseHeaders() != null)
			{

				System.out.println( "Request ID: " + exception.getResponseHeaders().get( "x-amzn-RequestId"));
			}

		}
		catch( Exception exception)
		{
			exception.printStackTrace();
		}
		return new ProductSearchResult();
	}

	private void convertAndSortPrices(List<Product> productsList, String preferedCurrency)
	{
		productsList.forEach( pr -> {
			if( pr.getBuyingInfo() == null)
			{ return; }
			List<BuyingInfo> sortedBuyingInfosList = (List<BuyingInfo>)pr.getBuyingInfo();
			CurrencyUtils.convertPrices( sortedBuyingInfosList, preferedCurrency);
			sortedBuyingInfosList.sort( (BuyingInfo b1, BuyingInfo b2) -> b1.getAmount().compareTo( b2.getAmount()));
			pr.setBuyingInfo( sortedBuyingInfosList);
		});
	}

	private void saveInformationsToProduct(Long userId, SearchItemsResponse response, List<Product> productsList,
		List<String> productsAsinsList, String flag)
	{
		response.getSearchResult().getItems().forEach( pr -> {
			Product product = new Product();
			if( userId != -1)
			{
				try
				{
					User user = userRepository.getOne( userId);
					user.getFavoriteProducts().forEach( p -> {
						Product savedProduct = prodcutRepository.getOne( p.getId());
						if( pr.getASIN() != null && savedProduct.getAsin().contentEquals( pr.getASIN()))
						{
							product.setFavorite( Boolean.TRUE);
							return;
						}
					});
				}
				catch( Exception e)
				{
					// do nothing if you can't get the user
				}

			}
			if( pr.getASIN() != null)
			{
				product.setAsin( pr.getASIN());
				productsAsinsList.add( pr.getASIN());
			}

			product.setImages( getProductImages( pr.getImages()));
			if( pr.getItemInfo() != null && pr.getItemInfo().getTitle() != null && pr.getItemInfo().getTitle().getDisplayValue() != null)
			{
				product.setProductName( pr.getItemInfo().getTitle().getDisplayValue());
			}

			// TEST

			BuyingInfo buyingInfo = new BuyingInfo();
			if( pr.getDetailPageURL() != null)
			{
				buyingInfo.setBuyUrl( pr.getDetailPageURL());
			}

			if( pr.getOffers() != null && pr.getOffers().getListings() != null && pr.getOffers().getListings().get( 0).getPrice() != null
				&& pr.getOffers().getListings().get( 0).getPrice().getDisplayAmount() != null
				&& pr.getOffers().getListings().get( 0).getPrice().getAmount() != null
				&& pr.getOffers().getListings().get( 0).getPrice().getCurrency() != null)
			{
				buyingInfo.setPrice( pr.getOffers().getListings().get( 0).getPrice().getDisplayAmount());
				buyingInfo.setCurrency( pr.getOffers().getListings().get( 0).getPrice().getCurrency());
				if( pr.getOffers().getListings().get( 0).getPrice().getAmount() != null)
				{
					buyingInfo.setAmount( pr.getOffers().getListings().get( 0).getPrice().getAmount());

				}
			}

			// sort prices
			if( buyingInfo.getBuyUrl() != null && buyingInfo.getAmount() != null)
			{
				buyingInfo.setFlag( flag);
				product.getBuyingInfo().add( buyingInfo);
				productsList.add( product);
			}

		});
	}

	private void getPricesFromSecondaryAccounts(SearchFilter searchFilter, List<Product> productsList, List<String> productsAsinsList)
	{
		List<GetItemsResource> getItemsResources = setResourcesToGetRequest();
		List<Account> secondayAccounts = accountRepository.loadSecondaryAccounts( searchFilter.getPrimaryCountry(), Boolean.TRUE).stream()
			.filter( acc -> searchFilter.getShowPricesFromList().contains( acc.getLocaleCode())).collect( Collectors.toList());
		for( int i = 0; i < secondayAccounts.size(); i++)
		{
			GetItemsRequest getItemsRequest = new GetItemsRequest();
			getItemsRequest.setPartnerType( PartnerType.ASSOCIATES);
			getItemsRequest.setPartnerTag( secondayAccounts.get( i).getTag());
			getItemsRequest.setMarketplace( secondayAccounts.get( i).getMarketPlace());
			getItemsRequest.setCurrencyOfPreference( getCurrency( secondayAccounts.get( i).getLocaleCode()));
			getItemsRequest.itemIds( productsAsinsList);
			getItemsRequest.resources( getItemsResources);
			try
			{
				ApiClient newClient = apiClientService.getApiClientForLocale( secondayAccounts.get( i).getLocaleCode());
				GetItemsResponse getItemsResponse = new DefaultApi( newClient).getItems( getItemsRequest);
				if( getItemsResponse.getItemsResult() != null)
				{
					for( int j = 0; j < getItemsResponse.getItemsResult().getItems().size(); j++)
					{
						Item item = getItemsResponse.getItemsResult().getItems().get( j);
						if( item.getOffers() != null && item.getOffers().getListings() != null
							&& item.getOffers().getListings().get( 0).getPrice() != null
							&& item.getOffers().getListings().get( 0).getPrice().getDisplayAmount() != null
							&& item.getOffers().getListings().get( 0).getPrice().getAmount() != null
							&& item.getOffers().getListings().get( 0).getPrice().getCurrency() != null)
						{
							BuyingInfo newBuyingInfo = new BuyingInfo();
							newBuyingInfo.setPrice( item.getOffers().getListings().get( 0).getPrice().getDisplayAmount());
							newBuyingInfo.setAmount( item.getOffers().getListings().get( 0).getPrice().getAmount());
							newBuyingInfo.setCurrency( item.getOffers().getListings().get( 0).getPrice().getCurrency());
							if( item.getDetailPageURL() != null)
							{
								newBuyingInfo.setBuyUrl( item.getDetailPageURL());
								newBuyingInfo.setFlag( secondayAccounts.get( i).getLocaleCode());
								productsList.forEach( pr -> {
									if( pr.getAsin().contentEquals( item.getASIN()) && newBuyingInfo.getAmount() != null)
									{
										pr.getBuyingInfo().add( newBuyingInfo);
									}
								});
							}

						}
					}

				}
			}
			catch( Exception e)
			{
				System.out.println( "error getting items response");
			}

		}
	}

	private List<GetItemsResource> setResourcesToGetRequest()
	{
		List<GetItemsResource> getItemsResources = new ArrayList<GetItemsResource>();
		getItemsResources.add( GetItemsResource.OFFERS_LISTINGS_PRICE);
		return getItemsResources;
	}

	private void setSearchCriteria(SearchFilter searchFilter, SearchItemsRequest searchItemsRequest)
	{
		if( searchFilter.getItemCount() > 0)
		{
			searchItemsRequest.setItemCount( searchFilter.getItemCount());
		}
		if( searchFilter.getItemPage() > 0)
		{
			searchItemsRequest.setItemPage( searchFilter.getItemPage());
		}
		if( !searchFilter.getKeyword().isEmpty())
		{
			searchItemsRequest.setKeywords( searchFilter.getKeyword());
		}
		if( !searchFilter.getAvailability().isEmpty())
		{
			searchItemsRequest.setAvailability( Availability.fromValue( searchFilter.getAvailability()));
		}
		if( !searchFilter.getCondition().isEmpty())
		{
			searchItemsRequest.setCondition( Condition.fromValue( searchFilter.getCondition()));
		}
		if( !searchFilter.getCurrencyOfPreference().isEmpty())
		{
			searchItemsRequest.setCurrencyOfPreference( getCurrency( searchFilter.getPrimaryCountry()));
		}
		if( !searchFilter.getDeliveryFlags().isEmpty())
		{
			List<DeliveryFlag> deliveryFlags = searchFilter.getDeliveryFlags().stream().map( d -> DeliveryFlag.fromValue( d))
				.collect( Collectors.toList());
			searchItemsRequest.setDeliveryFlags( deliveryFlags);
		}
		if( searchFilter.getMinReviewsRating() > 0)
		{
			searchItemsRequest.setMinReviewsRating( searchFilter.getMinReviewsRating());
		}
		if( searchFilter.getMinPrice() > 0)
		{
			int price = CurrencyUtils.ConvertSingle( searchFilter.getMinPrice(), getCurrency( searchFilter.getPrimaryCountry()),
				searchFilter.getCurrencyOfPreference());
			searchItemsRequest.setMinPrice( 100 * Integer.valueOf( price));
		}
		if( searchFilter.getMaxPrice() > 0)
		{
			if( searchFilter.getMinPrice() >= 0 && searchFilter.getMaxPrice() > searchFilter.getMinPrice())
			{
				int price = CurrencyUtils.ConvertSingle( searchFilter.getMaxPrice(), getCurrency( searchFilter.getPrimaryCountry()),
					searchFilter.getCurrencyOfPreference());
				searchItemsRequest.setMaxPrice( 100 * Integer.valueOf( price));
			}

		}
		if( !searchFilter.getSortBy().isEmpty())
		{
			searchItemsRequest.setSortBy( SortBy.fromValue( searchFilter.getSortBy()));
		}
	}

	private List<SearchItemsResource> setResourcesToRequest()
	{
		List<SearchItemsResource> searchItemsResources = new ArrayList<SearchItemsResource>();
		searchItemsResources.add( SearchItemsResource.ITEMINFO_TITLE);
		searchItemsResources.add( SearchItemsResource.OFFERS_LISTINGS_PRICE);
		searchItemsResources.add( SearchItemsResource.IMAGES_PRIMARY_LARGE);
		searchItemsResources.add( SearchItemsResource.IMAGES_VARIANTS_LARGE);
		return searchItemsResources;
	}

	private List<String> getProductImages(Images images)
	{
		List<String> imagesList = new ArrayList<>();
		if( images.getPrimary() != null)
		{
			imagesList.add( images.getPrimary().getLarge().getURL());
		}
		if( images.getVariants() != null)
		{
			images.getVariants().forEach( iv -> imagesList.add( iv.getLarge().getURL()));
		}
		return imagesList;
	}

	private String getCurrency(String localeCode)
	{
		switch( localeCode)
		{
		case "UK":
			return "GBP";
		case "USA":
			return "USD";
		default:
			return "EUR";

		}
	}
}
