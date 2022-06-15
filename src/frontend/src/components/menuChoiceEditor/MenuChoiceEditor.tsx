import React from "react";
import {Button, Option, Row, Select, TextInput, Text, Heading} from "@foreflight/ffui";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";
import {addMenuChoice, deleteMenuChoice, updateMenuChoice} from "../../client";
import MenuChoiceList from "../menuChoiceList/MenuChoiceList";
import MenuChoice from "../../types/MenuChoice";
import MenuChoiceDTO from "../../types/MenuChoiceDTO";
import resource from "../../types/Resource";
import Resource from "../../types/Resource";
import {Buffer} from "buffer";

type MenuChoiceEditorProps = {
    choiceBeingEdited: MenuChoiceWithChildren | null
    allChoices: MenuChoiceWithChildren[]
    allResources: Resource[]
    cancelCallback: () => void;
    saveCallback: () => void;
}

type MenuChoiceEditorState = {
    addingNewChoice: boolean
    editingChoice: boolean
    nameInputValue: string
    parentNameInputValue: string | null
    resourceNames: string[]
}

class MenuChoiceEditor extends React.Component<MenuChoiceEditorProps, MenuChoiceEditorState>{

    constructor(props: MenuChoiceEditorProps) {
        super(props);

        this.state = {
            addingNewChoice: false,
            editingChoice: this.props.choiceBeingEdited !== null,
            nameInputValue: (this.props.choiceBeingEdited?.name == undefined) ? "" : this.props.choiceBeingEdited.name,
            parentNameInputValue: (this.props.choiceBeingEdited?.parent?.name == undefined) ? null : this.props.choiceBeingEdited.parent.name,
            resourceNames: (this.props.choiceBeingEdited?.resources == undefined) ? [] : this.props.choiceBeingEdited.resources.map(resource => resource.name)
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

        if(this.state.addingNewChoice){
            this.cancelFunc();
        }
        else {
            this.props.cancelCallback();
        }
        this.props.saveCallback();
    }

    async deleteChoice(deleteId: number){
        await deleteMenuChoice(deleteId);

        this.props.cancelCallback();
        this.props.saveCallback();
    }

    cancelFunc() {
        this.setState({
            addingNewChoice: false,
            editingChoice: false,
            nameInputValue: "",
            parentNameInputValue: null,
            resourceNames: []
        })
    }

    getParentOptions() : Option[] {
        let options: Option[] = [];

        for(const choice of this.props.allChoices){
            if(choice.id != this.props.choiceBeingEdited?.id) {
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
                if(resource.name == existResourceName){
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

    render() {
        return (
          <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
              <Heading>
                  Menu Choice Editor:
              </Heading>
              <p>
                  Currently Editing: <span style={{color: "#3498DB"}}>{
                      this.state.addingNewChoice ? "ADDING A NEW CHOICE" : this.state.editingChoice ? this.props.choiceBeingEdited?.name : ""}</span>
              </p>

              <Row>
                  <Button disabled={this.state.addingNewChoice || this.state.editingChoice}
                          onClick={() => {
                              this.setState({
                                  addingNewChoice: true
                              })
                          }}
                  >
                      Add New Choice
                  </Button>
                  <Button color={"red"}
                          disabled={!this.state.editingChoice}
                          onClick={() => {
                              this.deleteChoice(this.props.choiceBeingEdited!.id)
                          }}
                  >
                      Delete This Choice
                  </Button>
              </Row>

              <Row width={"60%"} margin={"10px"}>
                  <TextInput label={"Name"}
                             disabled={!this.state.editingChoice && !this.state.addingNewChoice}
                             value={this.state.nameInputValue}
                             onChange={newValue => {
                                 this.setState({nameInputValue: newValue})
                             }}
                  />
              </Row>

              <Row width={"60%"} margin={"10px"}  flexAlign={"center"}>
                  <Select autoComplete
                          options={this.getParentOptions()}
                          disabled={!this.state.editingChoice && !this.state.addingNewChoice}
                          value={this.state.parentNameInputValue}
                          label={"Parent Name"}
                          onChange={(newValue: string | null) => {
                              this.setState({
                                  parentNameInputValue: newValue
                              })
                          }}
                  />

                  <Button color={"orange"}
                          disabled={!this.state.editingChoice && !this.state.addingNewChoice}
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
                          disabled={!this.state.editingChoice && !this.state.addingNewChoice}
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
                  <Button color={"green"} disabled={!this.state.editingChoice && !this.state.addingNewChoice} onClick={() => {
                      if(this.state.editingChoice){
                          this.saveModifiedChoice(this.props.choiceBeingEdited!.id)
                      }
                      else{
                          this.saveModifiedChoice(null);
                      }
                  }}>
                      Save
                  </Button>
                  <Button color={"red"} disabled={!this.state.editingChoice && !this.state.addingNewChoice} onClick={() => {
                      if(this.state.addingNewChoice){
                          this.cancelFunc();
                      }
                      else {
                          this.props.cancelCallback();
                      }
                  }}>
                      Cancel
                  </Button>
              </Row>

          </Row>
        );
    }
}

export default MenuChoiceEditor;