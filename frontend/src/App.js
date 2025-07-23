import React, { useState, useEffect } from 'react';
import './App.css';
import ProductList from './components/ProductList';
import ProductDetail from './components/ProductDetail';
import AddProduct from './components/AddProduct';
import EditProduct from './components/EditProduct';

function App() {
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [view, setView] = useState('list'); // 'list', 'detail', 'add', 'edit'
  const [productToEdit, setProductToEdit] = useState(null);

  const fetchProducts = () => {
    setLoading(true);
    fetch('http://localhost:8080/api/products')
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(response => {
        if (response.status === 'SUCCESS') {
          setProducts(response.data);
          setLoading(false);
        } else {
          throw new Error(response.message || 'Failed to load products');
        }
      })
      .catch(error => {
        console.error('Error fetching products:', error);
        setError('Failed to load products. Please try again later.');
        setLoading(false);
      });
  };

  useEffect(() => {
    // Fetch products from the backend API
    fetchProducts();
  }, []);

  const handleProductSelect = (product) => {
    setSelectedProduct(product);
    setView('detail');
  };

  const handleBackToList = () => {
    setSelectedProduct(null);
    setProductToEdit(null);
    setView('list');
  };

  const handleAddProduct = () => {
    setView('add');
  };

  const handleEditProduct = (product) => {
    setProductToEdit(product);
    setView('edit');
  };

  const handleProductAdded = (newProduct) => {
    // Add the new product to the list or refresh the list
    setProducts([...products, newProduct]);
    // Alternatively, refetch all products
    // fetchProducts();
  };

  const handleProductUpdated = (updatedProduct) => {
    // Update the product in the list
    setProducts(products.map(p => 
      p.id === updatedProduct.id ? updatedProduct : p
    ));
    // Alternatively, refetch all products
    // fetchProducts();
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
        ) : view === 'detail' && selectedProduct ? (
          <ProductDetail product={selectedProduct} onBack={handleBackToList} />
        ) : view === 'add' ? (
          <AddProduct 
            onBack={handleBackToList} 
            onProductAdded={handleProductAdded} 
          />
        ) : view === 'edit' && productToEdit ? (
          <EditProduct 
            product={productToEdit} 
            onBack={handleBackToList} 
            onProductUpdated={handleProductUpdated} 
          />
        ) : (
          <ProductList 
            products={products} 
            onSelectProduct={handleProductSelect} 
            onAddProduct={handleAddProduct}
            onEditProduct={handleEditProduct}
          />
        )}
      </main>
      <footer className="App-footer">
        <p>&copy; 2025 Online Store</p>
      </footer>
    </div>
  );
}

export default App;