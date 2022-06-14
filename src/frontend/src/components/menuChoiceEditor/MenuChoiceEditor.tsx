import React from "react";
import {Button, Row, TextInput} from "@foreflight/ffui";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";
import {updateMenuChoice} from "../../client";
import MenuChoiceList from "../menuChoiceList/MenuChoiceList";
import MenuChoice from "../../types/MenuChoice";
import MenuChoiceDTO from "../../types/MenuChoiceDTO";
import resource from "../../types/Resource";

type MenuChoiceEditorProps = {
    editingChoice: MenuChoiceWithChildren | null
    cancelCallback: () => void;
    saveCallback: () => void;
}

type MenuChoiceEditorState = {
    nameInputValue: string
}

class MenuChoiceEditor extends React.Component<MenuChoiceEditorProps, MenuChoiceEditorState>{

    constructor(props: MenuChoiceEditorProps) {
        super(props);

        this.state = {
            nameInputValue: ""
        }
    }

    async saveModifiedChoice(choice: MenuChoiceWithChildren) {
        let newChoice: MenuChoiceDTO = {
            choiceName: choice.name,
            parentName: choice.parent ? choice.parent.name : null,
            resourceNames: choice.resources.map(resource => {
                return resource.name;
            })
        }

        if (this.state.nameInputValue != "") {
            newChoice.choiceName = this.state.nameInputValue;
        }

        await updateMenuChoice(choice.id, newChoice);

        this.props.cancelCallback();
        this.props.saveCallback();
    }

    render() {
        return (
          <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
              Menu Choice Editor:
              <Row width={"60%"}>
              <TextInput label={"Name"} disabled={!this.props.editingChoice} value={this.props.editingChoice ? this.props.editingChoice.name : ""} onChange={newValue => {
                  this.setState({nameInputValue: newValue})
              }}/>
              </Row>

              <Row flexJustify={"flex-end"} width={"100%"}>
                  <Button color={"green"} disabled={!this.props.editingChoice} onClick={() => {
                      this.saveModifiedChoice(this.props.editingChoice!)
                  }}>
                      Save
                  </Button>
                  <Button color={"red"} disabled={!this.props.editingChoice} onClick={() => {
                      this.props.cancelCallback();
                  }}>
                      Cancel
                  </Button>
              </Row>

          </Row>
        );
    }
}

export default MenuChoiceEditor;