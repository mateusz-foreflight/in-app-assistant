import React from "react";
import MenuPreview from "../components/menu/MenuPreview";


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