import React, { useState } from 'react';
import './ProductDetail.css';

const ProductDetail = ({ product, onBack }) => {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  if (!product) {
    return <div className="product-detail-error">Product not found</div>;
  }

  return (
    <div className="product-detail">
      <button className="back-button" onClick={onBack}>
        &larr; Back to Products
      </button>
      
      <div className="product-detail-content">
        <div className="product-detail-images">
          <div className="main-image">
            {product.images && product.images.length > 0 ? (
              <img 
                src={product.images[selectedImageIndex]} 
                alt={`${product.title} - view ${selectedImageIndex + 1}`} 
              />
            ) : (
              <div className="no-image">No Image Available</div>
            )}
          </div>
          
          {product.images && product.images.length > 1 && (
            <div className="image-thumbnails">
              {product.images.map((image, index) => (
                <div 
                  key={index}
                  className={`thumbnail ${index === selectedImageIndex ? 'active' : ''}`}
                  onClick={() => setSelectedImageIndex(index)}
                >
                  <img src={image} alt={`${product.title} thumbnail ${index + 1}`} />
                </div>
              ))}
            </div>
          )}
        </div>
        
        <div className="product-detail-info">
          <h1>{product.title}</h1>
          <div className="product-detail-price">${product.price}</div>
          <div className="product-detail-description">
            <h3>Description</h3>
            <p>{product.description}</p>
          </div>
          <button className="add-to-cart-button">Add to Cart</button>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;