import React from "react";
import MenuPreview from "../components/menuPreview/MenuPreview";


class MenuPreviewPage extends React.Component<{},{}>{

    render(){
        return (
          <div>
              Menu Preview:

              <MenuPreview></MenuPreview>
          </div>
        );
    }
}

export default MenuPreviewPage;