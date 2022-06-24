import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MenuPreviewPage from "./pages/MenuPreviewPage";
import MenuModificationPage from "./pages/MenuModificationPage";
import ResourceModificationPage from "./pages/ResourceModificationPage";
import SourceModificationPage from "./pages/SourceModificationPage";
import UserPreviewPage from "./pages/UserPreviewPage";

function App() {
  return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<MenuPreviewPage/>}/>
            <Route path="/userPreview" element={<UserPreviewPage/>}/>
            <Route path="/menuModification" element={<MenuModificationPage/>}/>
            <Route path="/resourceModification" element={<ResourceModificationPage/>}/>
            <Route path="/sourceModification" element={<SourceModificationPage/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
