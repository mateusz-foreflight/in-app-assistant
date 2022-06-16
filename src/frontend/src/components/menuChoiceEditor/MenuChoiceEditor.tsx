import React from "react";
import {Button, Option, Row, Select, TextInput, Heading} from "@foreflight/ffui";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";
import {addMenuChoice, deleteMenuChoice, updateMenuChoice} from "../../client";
import MenuChoiceDTO from "../../types/MenuChoiceDTO";
import Resource from "../../types/Resource";


enum editAddState {
    editing,
    adding,
    inactive
}

type MenuChoiceEditorProps = {
    choiceBeingEdited: MenuChoiceWithChildren | null
    allChoices: MenuChoiceWithChildren[]
    allResources: Resource[]
    deactivateCallback: () => void;
    saveCallback: () => void;
}

type MenuChoiceEditorState = {
    editingAdding: editAddState
    nameInputValue: string
    parentNameInputValue: string | null
    resourceNames: string[]
}

class MenuChoiceEditor extends React.Component<MenuChoiceEditorProps, MenuChoiceEditorState>{

    constructor(props: MenuChoiceEditorProps) {
        super(props);

        this.state = {
            editingAdding: this.props.choiceBeingEdited ? editAddState.editing : editAddState.inactive,
            nameInputValue: (this.props.choiceBeingEdited?.name === undefined) ? "" : this.props.choiceBeingEdited.name,
            parentNameInputValue: (this.props.choiceBeingEdited?.parent?.name === undefined) ? null : this.props.choiceBeingEdited.parent.name,
            resourceNames: (this.props.choiceBeingEdited?.resources === undefined) ? [] : this.props.choiceBeingEdited.resources.map(resource => resource.name)
        }
    }

    async saveModifiedChoice(updateId: number | null) {
        let newChoice: MenuChoiceDTO = {
            choiceName: this.state.nameInputValue,
            parentName: this.state.parentNameInputValue,
            resourceNames: this.state.resourceNames
        }

        if(newChoice.choiceName === ""){
            return;
        }

        if(updateId){
            await updateMenuChoice(updateId, newChoice);
        }
        else{
            await addMenuChoice(newChoice);
        }

        if(this.state.editingAdding === editAddState.adding){
            this.cancelFunc();
        }
        else {
            this.props.deactivateCallback();
        }
        this.props.saveCallback();
    }

    async deleteChoice(deleteId: number){
        await deleteMenuChoice(deleteId);

        this.props.deactivateCallback();
        this.props.saveCallback();
    }

    cancelFunc() {
        this.setState({
            editingAdding: editAddState.inactive,
            nameInputValue: "",
            parentNameInputValue: null,
            resourceNames: []
        })

        this.props.deactivateCallback();
    }

    getParentOptions() : Option[] {
        let options: Option[] = [];

        for(const choice of this.props.allChoices){
            if(choice.id !== this.props.choiceBeingEdited?.id) {
                options.push({
                    label: choice.name,
                    value: choice.name
                })
            }
        }

        return options;
    }

    getResourceOptions() : Option[] {
        let options: Option[] = [];

        for(const resource of this.props.allResources){
            let addResource = true;
            for(const existResourceName of this.state.resourceNames){
                if(resource.name === existResourceName){
                    addResource = false;
                    break;
                }
            }

            if(addResource){
                options.push({
                    label: resource.name,
                    value: resource.name
                })
            }
        }

        return options;
    }

    getCurrentlyEditingText(){
        switch(this.state.editingAdding){
            case editAddState.adding:
                return "ADDING NEW CHOICE"
            case editAddState.editing:
                return this.props.choiceBeingEdited?.name
            case editAddState.inactive:
                return "";
        }
    }

    render() {
        return (
          <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
              <Heading>
                  Menu Choice Editor:
              </Heading>
              <p>
                  Currently Editing: <span style={{color: "#3498DB"}}>{
                     this.getCurrentlyEditingText()}</span>
              </p>

              <Row>
                  <Button disabled={this.state.editingAdding === editAddState.adding || this.state.editingAdding === editAddState.editing}
                          onClick={() => {
                              this.setState({
                                  editingAdding: editAddState.adding
                              })
                          }}
                  >
                      Add New Choice
                  </Button>
                  <Button color={"red"}
                          disabled={!(this.state.editingAdding === editAddState.editing)}
                          onClick={() => {
                              this.deleteChoice(this.props.choiceBeingEdited!.id)
                          }}
                  >
                      Delete This Choice
                  </Button>
              </Row>

              <Row width={"60%"} margin={"10px"}>
                  <TextInput label={"Name"}
                             disabled={this.state.editingAdding === editAddState.inactive}
                             value={this.state.nameInputValue}
                             onChange={newValue => {
                                 this.setState({nameInputValue: newValue})
                             }}
                  />
              </Row>

              <Row width={"60%"} margin={"10px"}  flexAlign={"center"}>
                  <Select autoComplete
                          options={this.getParentOptions()}
                          disabled={this.state.editingAdding === editAddState.inactive}
                          value={this.state.parentNameInputValue}
                          label={"Parent Name"}
                          onChange={(newValue: string | null) => {
                              this.setState({
                                  parentNameInputValue: newValue
                              })
                          }}
                  />

                  <Button color={"orange"}
                          disabled={this.state.editingAdding === editAddState.inactive}
                          onClick={() => {
                              this.setState({
                                  parentNameInputValue: null
                              })
                          }}
                  >
                      Clear
                  </Button>
              </Row>

              <Row width={"60%"} margin={"10px"}>
                  <Select autoComplete
                          disabled={this.state.editingAdding === editAddState.inactive}
                          label={"Add Resource"}
                          options={this.getResourceOptions()}
                          onChange={(newValue: string) => {
                              this.setState(prevState => ({
                                  resourceNames: [...prevState.resourceNames, newValue]
                              }))
                          }}
                  />
              </Row>

              <Row>
                  Resources:
                  {this.state.resourceNames.map((resource) => (
                      <Button
                          key={resource}
                          small={true}
                          color={"gray"}
                          onClick={() => {
                              this.setState({
                                  resourceNames: this.state.resourceNames.filter(existResource => resource !== existResource )
                              })
                          }}
                      >
                          {resource}
                      </Button>
                  ))}

              </Row>

              <Row flexJustify={"flex-end"} width={"100%"}>
                  <Button color={"green"} disabled={this.state.editingAdding === editAddState.inactive} onClick={() => {
                      if(this.state.editingAdding === editAddState.editing){
                          this.saveModifiedChoice(this.props.choiceBeingEdited!.id)
                      }
                      else{
                          this.saveModifiedChoice(null);
                      }
                  }}>
                      Save
                  </Button>
                  <Button color={"red"} disabled={this.state.editingAdding === editAddState.inactive} onClick={() => {
                      this.cancelFunc();
                  }}>
                      Cancel
                  </Button>
              </Row>

          </Row>
        );
    }
}

export default MenuChoiceEditor;