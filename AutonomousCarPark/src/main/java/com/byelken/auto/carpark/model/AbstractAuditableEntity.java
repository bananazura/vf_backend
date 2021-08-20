/**
 * 
 */
package com.byelken.auto.carpark.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Berkay.Yelken
 *
 */

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<ID> extends AbstractPersistableEntity<ID> implements Serializable
{
	private static final long serialVersionUID = 3972209583438390799L;

	@CreatedDate
	@Column(name = "CREATED_DATE")
	protected LocalDate createdDate;

	@LastModifiedDate
	@Column(name = "MODIFIED_DATE")
	protected LocalDate lastModifiedDate;

	@CreatedBy
	@AttributeOverride(name = "EMAIL", column = @Column(name = "CREATED_BY"))
	@Embedded
	protected String createdBy;

	@LastModifiedBy
	@AttributeOverride(name = "EMAIL", column = @Column(name = "LAST_MODIFIED_BY"))
	@Embedded
	protected String lastModifiedBy;

}
