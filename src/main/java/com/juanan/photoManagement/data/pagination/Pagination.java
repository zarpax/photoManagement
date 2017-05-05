package com.juanan.photoManagement.data.pagination;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pagination {
	private Integer pagInicio					= null;
	private Integer maxResultados					= null;
	private List<QueryParameters> search	= null;
	private List<OrderParameters> order		= null;

	public Integer getPagInicio() {
		return pagInicio;
	}
	
	public void setPagInicio(Integer pagInicio) {
		this.pagInicio = pagInicio;
	}
	
	public Integer getMaxResultados() {
		return maxResultados;
	}
	
	public void setMaxResultados(Integer maxResultados) {
		this.maxResultados = maxResultados;
	}

	public List<QueryParameters> getSearch() {
		return search;
	}

	public void setSearch(List<QueryParameters> search) {
		this.search = search;
	}
	
	@JsonIgnore
	public String getWhere() {
		StringBuffer where = null;
		
		if (search != null && search.size()>0) {
			where = new StringBuffer(100);
			int tamano = search.size();
			for (int i = 0; i < tamano; i++) {				
				where.append((i != 0 ? " AND " : "") + search.get(i).toString());
				
				if (search.get(i).getValue() != null) {
					where.append((i + 1));
				}
			}
		}
		
		return (where!=null) ? where.toString() : null;
	}
	
	public List<OrderParameters> getOrder() {
		return order;
	}

	public void setOrder(List<OrderParameters> order) {
		this.order = order;
	}
	
	@JsonIgnore
	public String getOrderBy() {
		StringBuffer buffer = new StringBuffer(100);
		
		if (order != null) {
			int tamano = order.size();
			for (int i = 0; i < tamano; i++) {				
				buffer.append((i != 0 ? ", " : "") + order.get(i).toString());
			}

			return buffer.toString();
		}
		
		return null;
	}

	@Override
	public String toString() {
		StringBuffer builder = new StringBuffer();
		builder.append("Paginacion [pagInicio=").append(pagInicio).append(", maxResultados=").append(maxResultados)
		.append(", search=").append(search).append(", order=").append(order).append("]");
		return builder.toString();
	}
}
