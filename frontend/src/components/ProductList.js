import React from 'react';
import './ProductList.css';

const ProductList = ({ products, onSelectProduct, onAddProduct, onEditProduct }) => {
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
    </div>
  );
};

export default ProductList;