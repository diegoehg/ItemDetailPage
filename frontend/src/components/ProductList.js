import React from 'react';
import './ProductList.css';

const ProductList = ({ products, onSelectProduct }) => {
  if (products.length === 0) {
    return <p className="no-products">No products available.</p>;
  }

  return (
    <div className="product-list">
      <h2>Available Products</h2>
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
              <button className="view-details-btn">View Details</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;