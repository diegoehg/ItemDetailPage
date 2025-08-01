import React, { useState, useEffect } from 'react';
import './ProductDetail.css'; // For layout structure
import './ProductForm.css'; // For form-specific styles
import { API_ENDPOINTS } from '../config';

const AddProduct = ({ onBack, onProductAdded }) => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    price: '',
    imageUrl: '', // For adding a single image at a time
    sellerId: ''
  });
  
  const [images, setImages] = useState([]);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null);
  const [sellers, setSellers] = useState([]);
  const [loadingSellers, setLoadingSellers] = useState(false);
  
  // Fetch sellers when component mounts
  useEffect(() => {
    const fetchSellers = async () => {
      setLoadingSellers(true);
      try {
        const response = await fetch(API_ENDPOINTS.SELLERS);
        const responseData = await response.json();
        
        if (responseData.status === 'SUCCESS') {
          setSellers(responseData.data);
        } else {
          console.error('Error fetching sellers:', responseData.message);
        }
      } catch (error) {
        console.error('Error fetching sellers:', error);
      } finally {
        setLoadingSellers(false);
      }
    };
    
    fetchSellers();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
    
    // Clear error when field is edited
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: null
      });
    }
  };

  const addImage = () => {
    if (formData.imageUrl.trim()) {
      setImages([...images, formData.imageUrl.trim()]);
      setFormData({
        ...formData,
        imageUrl: ''
      });
    }
  };

  const removeImage = (index) => {
    setImages(images.filter((_, i) => i !== index));
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.title.trim()) {
      newErrors.title = 'Title is required';
    }
    
    if (!formData.price.trim()) {
      newErrors.price = 'Price is required';
    } else if (isNaN(parseFloat(formData.price)) || parseFloat(formData.price) <= 0) {
      newErrors.price = 'Price must be a positive number';
    }
    
    if (!formData.sellerId) {
      newErrors.sellerId = 'Seller is required';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsSubmitting(true);
    setSubmitError(null);
    
    try {
      const productData = {
        title: formData.title,
        description: formData.description,
        price: parseFloat(formData.price),
        images: images,
        seller: formData.sellerId ? { id: parseInt(formData.sellerId) } : null
      };
      
      const response = await fetch(API_ENDPOINTS.PRODUCTS, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
      });
      
      const responseData = await response.json();
      
      if (responseData.status !== 'SUCCESS') {
        throw new Error(responseData.message || `Error: ${response.status}`);
      }
      
      const savedProduct = responseData.data;
      
      // Call the callback to inform parent component
      if (onProductAdded) {
        onProductAdded(savedProduct);
      }
      
      // Go back to product list
      onBack();
    } catch (error) {
      console.error('Error adding product:', error);
      setSubmitError('Failed to add product. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="product-detail">
      <button className="back-button" onClick={onBack}>
        &larr; Back to Products
      </button>
      
      <div className="product-detail-content">
        <div className="product-form">
          <h2>Add New Product</h2>
          
          {submitError && (
            <div className="error-message">{submitError}</div>
          )}
          
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="title">Title *</label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                className={errors.title ? 'error' : ''}
              />
              {errors.title && <div className="error-text">{errors.title}</div>}
            </div>
            
            <div className="form-group">
              <label htmlFor="description">Description</label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows="4"
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="price">Price *</label>
              <input
                type="text"
                id="price"
                name="price"
                value={formData.price}
                onChange={handleChange}
                className={errors.price ? 'error' : ''}
              />
              {errors.price && <div className="error-text">{errors.price}</div>}
            </div>
            
            <div className="form-group">
              <label htmlFor="sellerId">Seller *</label>
              <select
                id="sellerId"
                name="sellerId"
                value={formData.sellerId}
                onChange={handleChange}
                className={errors.sellerId ? 'error' : ''}
                disabled={loadingSellers}
              >
                <option value="">Select a seller</option>
                {sellers.map(seller => (
                  <option key={seller.id} value={seller.id}>
                    {seller.name}
                  </option>
                ))}
              </select>
              {errors.sellerId && <div className="error-text">{errors.sellerId}</div>}
              {loadingSellers && <div className="loading-text">Loading sellers...</div>}
            </div>
            
            <div className="form-group">
              <label htmlFor="imageUrl">Image URL</label>
              <div className="image-input-container">
                <input
                  type="text"
                  id="imageUrl"
                  name="imageUrl"
                  value={formData.imageUrl}
                  onChange={handleChange}
                />
                <button 
                  type="button" 
                  onClick={addImage}
                  className="add-image-btn"
                >
                  Add Image
                </button>
              </div>
            </div>
            
            {images.length > 0 && (
              <div className="image-list">
                <h4>Added Images:</h4>
                <ul>
                  {images.map((url, index) => (
                    <li key={index}>
                      {url}
                      <button 
                        type="button" 
                        onClick={() => removeImage(index)}
                        className="remove-image-btn"
                      >
                        Remove
                      </button>
                    </li>
                  ))}
                </ul>
              </div>
            )}
            
            <div className="form-actions">
              <button 
                type="submit" 
                className="add-to-cart-button"
                disabled={isSubmitting}
              >
                {isSubmitting ? 'Adding...' : 'Add Product'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddProduct;