import React from "react";
import MenuPreview from "../components/menu/MenuPreview";
import {Table, TBody, Td, TdHeader, TFoot, THead, Tr} from "@foreflight/ffui";


class Home extends React.Component<{},{}>{

    render(){
        return (
          <div>
              Menu Preview:

              <MenuPreview></MenuPreview>
          </div>
        );
    }
}

export default Home;