package models.components;

public class PagingInfo {

	private int pageIndex;
	private int pageSize;
	private int pageCount;

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {

		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {

		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {

		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;

	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {

		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {

		this.pageCount = pageCount;
	}
}
