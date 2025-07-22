import React, { useState, useEffect } from 'react';
import './App.css';
import ProductList from './components/ProductList';
import ProductDetail from './components/ProductDetail';

function App() {
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch products from the backend API
    fetch('http://localhost:8080/api/products')
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setProducts(data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching products:', error);
        setError('Failed to load products. Please try again later.');
        setLoading(false);
      });
  }, []);

  const handleProductSelect = (product) => {
    setSelectedProduct(product);
  };

  const handleBackToList = () => {
    setSelectedProduct(null);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Online Store</h1>
      </header>
      <main className="App-main">
        {loading ? (
          <p className="loading-message">Loading products...</p>
        ) : error ? (
          <p className="error-message">{error}</p>
        ) : selectedProduct ? (
          <ProductDetail product={selectedProduct} onBack={handleBackToList} />
        ) : (
          <ProductList products={products} onSelectProduct={handleProductSelect} />
        )}
      </main>
      <footer className="App-footer">
        <p>&copy; 2025 Online Store</p>
      </footer>
    </div>
  );
}

export default App;