package com.ht.codegen.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *  TODO 视图注释表(真实表用表名,字段名)
 * @author fengchangyi@haitao-tech.com
 * @date 2015-1-4 下午2:37:28
 */
@Entity
@Table(name = "halo_view_comment")
public class HaloViewComment {
	@Id
	private String name;
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}



}
