
package com.workflow2.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This is the Entity class for Category table
 * @author Mayur_Jadhav
 * @version v0.0.1
 */
@Entity
@Table(name="product_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

	@Id
	@Column(name = "category_name", nullable = false)
	private String name;

	@Column(name = "category_image", unique = false, nullable = true, length = 16777215)
	private byte[] image;
	
	
}
