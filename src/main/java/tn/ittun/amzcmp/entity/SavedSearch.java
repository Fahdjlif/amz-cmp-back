
package tn.ittun.amzcmp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "SAVED_SEARCH_SINGLE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class SavedSearch
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long	id;
	private String														name;
	@Column(length = 3000) private String								value;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public Long getId()
	{
		return id;
	}

}
