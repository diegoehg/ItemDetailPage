import React, { useState } from 'react';
import './ProductDetail.css'; // For layout structure
import './ProductForm.css'; // For form-specific styles

const AddProduct = ({ onBack, onProductAdded }) => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    price: '',
    imageUrl: '' // For adding a single image at a time
  });
  
  const [images, setImages] = useState([]);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null);

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
        images: images
      };
      
      const response = await fetch('http://localhost:8080/api/products', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
      });
      
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
      
      const savedProduct = await response.json();
      
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