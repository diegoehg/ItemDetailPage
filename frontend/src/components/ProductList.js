import React from 'react';
import './ProductList.css';

const ProductList = ({ 
  products, 
  onSelectProduct, 
  onAddProduct, 
  onEditProduct,
  // Pagination props
  currentPage,
  pageSize,
  totalPages,
  totalElements,
  isFirstPage,
  isLastPage,
  onPageChange,
  onPageSizeChange,
  onNextPage,
  onPreviousPage
}) => {
  if (products.length === 0 && !onAddProduct) {
    return <p className="no-products">No products available.</p>;
  }

  // Handle edit button click without triggering the card click
  const handleEditClick = (e, product) => {
    e.stopPropagation(); // Prevent the card click event
    onEditProduct(product);
  };

  return (
    <div className="product-list">
      <div className="product-list-header">
        <h2>Available Products</h2>
        {onAddProduct && (
          <button className="add-product-btn" onClick={onAddProduct}>
            Add New Product
          </button>
        )}
      </div>
      
      <div className="products-grid">
        {products.map(product => (
          <div 
            key={product.id} 
            className="product-card" 
            onClick={() => onSelectProduct(product)}
          >
            <div className="product-image">
              {product.images && product.images.length > 0 ? (
                <img src={product.images[0]} alt={product.title} />
              ) : (
                <div className="no-image">No Image</div>
              )}
            </div>
            <div className="product-info">
              <h3>{product.title}</h3>
              <p className="product-price">${product.price}</p>
              <p className="product-description">
                {product.description && product.description.length > 100
                  ? `${product.description.substring(0, 100)}...`
                  : product.description}
              </p>
              <div className="product-actions">
                <button className="view-details-btn">View Details</button>
                {onEditProduct && (
                  <button 
                    className="edit-product-btn"
                    onClick={(e) => handleEditClick(e, product)}
                  >
                    Edit
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
      
      {/* Pagination Controls */}
      {totalPages > 0 && (
        <div className="pagination-controls">
          <div className="pagination-info">
            Showing page {currentPage} of {totalPages} ({totalElements} total items)
          </div>
          
          <div className="pagination-actions">
            <button 
              className="pagination-btn" 
              onClick={onPreviousPage} 
              disabled={isFirstPage}
            >
              Previous
            </button>
            
            <div className="page-size-selector">
              <label htmlFor="page-size">Items per page:</label>
              <select 
                id="page-size" 
                value={pageSize} 
                onChange={(e) => onPageSizeChange(Number(e.target.value))}
              >
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="100">100</option>
              </select>
            </div>
            
            <button 
              className="pagination-btn" 
              onClick={onNextPage} 
              disabled={isLastPage}
            >
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductList;