import React from "react";
import MenuPreview from "../components/menuPreview/MenuPreview";
import {Table, TBody, Td, TdHeader, TFoot, THead, Tr} from "@foreflight/ffui";


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