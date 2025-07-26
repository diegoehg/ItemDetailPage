import React, { useState } from 'react';
import './ProductDetail.css';

const ProductDetail = ({ product, onBack }) => {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('');

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
          
          {product.seller && (
            <div className="seller-info">
              <h3>Seller Information</h3>
              <p className="seller-name">{product.seller.name}</p>
              
              {product.seller.paymentMethods && product.seller.paymentMethods.length > 0 && (
                <div className="payment-methods">
                  <label htmlFor="payment-method-select">Payment Method:</label>
                  <select 
                    id="payment-method-select"
                    value={selectedPaymentMethod}
                    onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                    className="payment-method-select"
                  >
                    <option value="">Select a payment method</option>
                    {product.seller.paymentMethods.map((method) => (
                      <option key={method.id} value={method.id}>
                        {method.name}
                      </option>
                    ))}
                  </select>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;