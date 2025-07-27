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
  
  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [isFirstPage, setIsFirstPage] = useState(true);
  const [isLastPage, setIsLastPage] = useState(false);

  const fetchProducts = (page = currentPage, size = pageSize) => {
    setLoading(true);
    fetch(`http://localhost:8080/api/products?page=${page}&size=${size}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(response => {
        if (response.status === 'SUCCESS') {
          // Check if the response data has pagination information
          if (response.data.content) {
            // It's a paginated response
            const pagedData = response.data;
            setProducts(pagedData.content);
            setCurrentPage(pagedData.page);
            setPageSize(pagedData.size);
            setTotalPages(pagedData.totalPages);
            setTotalElements(pagedData.totalElements);
            setIsFirstPage(pagedData.first);
            setIsLastPage(pagedData.last);
          } else {
            // It's a regular list response
            setProducts(response.data);
          }
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
  
  // Pagination handlers
  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      fetchProducts(newPage, pageSize);
    }
  };
  
  const handlePageSizeChange = (newSize) => {
    // When changing page size, go back to first page
    fetchProducts(1, newSize);
  };
  
  const goToNextPage = () => {
    if (!isLastPage) {
      handlePageChange(currentPage + 1);
    }
  };
  
  const goToPreviousPage = () => {
    if (!isFirstPage) {
      handlePageChange(currentPage - 1);
    }
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
            // Pagination props
            currentPage={currentPage}
            pageSize={pageSize}
            totalPages={totalPages}
            totalElements={totalElements}
            isFirstPage={isFirstPage}
            isLastPage={isLastPage}
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            onNextPage={goToNextPage}
            onPreviousPage={goToPreviousPage}
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