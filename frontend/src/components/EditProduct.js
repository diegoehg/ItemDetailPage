import React, { useState, useEffect } from 'react';
import './ProductDetail.css'; // For layout structure
import './ProductForm.css'; // For form-specific styles

const EditProduct = ({ product, onBack, onProductUpdated }) => {
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

  // Initialize form with product data when component mounts or product changes
  useEffect(() => {
    if (product) {
      setFormData({
        title: product.title || '',
        description: product.description || '',
        price: product.price ? product.price.toString() : '',
        imageUrl: ''
      });
      
      setImages(product.images || []);
    }
  }, [product]);

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
        id: product.id, // Include the ID for the update
        title: formData.title,
        description: formData.description,
        price: parseFloat(formData.price),
        images: images
      };
      
      const response = await fetch(`http://localhost:8080/api/products/${product.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
      });
      
      const responseData = await response.json();
      
      if (responseData.status !== 'SUCCESS') {
        throw new Error(responseData.message || `Error: ${response.status}`);
      }
      
      const updatedProduct = responseData.data;
      
      // Call the callback to inform parent component
      if (onProductUpdated) {
        onProductUpdated(updatedProduct);
      }
      
      // Go back to product list
      onBack();
    } catch (error) {
      console.error('Error updating product:', error);
      setSubmitError('Failed to update product. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!product) {
    return <div className="product-detail-error">Product not found</div>;
  }

  return (
    <div className="product-detail">
      <button className="back-button" onClick={onBack}>
        &larr; Back to Products
      </button>
      
      <div className="product-detail-content">
        <div className="product-form">
          <h2>Edit Product</h2>
          
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
                <h4>Images:</h4>
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
                {isSubmitting ? 'Updating...' : 'Update Product'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditProduct;