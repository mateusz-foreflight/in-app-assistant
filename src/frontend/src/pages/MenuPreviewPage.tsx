import React from "react";
import MenuPreview from "../components/common/menuPreview/MenuPreview";
import Navbar from "../components/common/Navbar";


class MenuPreviewPage extends React.Component<{},{}>{

    render(){
        return (
          <div className={"page"}>
              <Navbar activePage={0}/>

              <MenuPreview></MenuPreview>
          </div>
        );
    }
}

export default MenuPreviewPage;