import React from "react";
import {Button, Option, Row, Select, TextInput, Heading, IError} from "@foreflight/ffui";
import MenuChoiceDTO from "../../types/MenuChoiceDTO";
import Resource from "../../types/Resource";
import MenuChoice from "../../types/MenuChoice";
import {cache} from "../common/Cache";
import {api} from "../../client";


enum modification {
    editing,
    adding,
    inactive
}

type MenuChoiceEditorProps = {
    choiceBeingEdited: MenuChoice | null
    deactivateCallback: () => void;
    saveCallback: () => void;
}

type MenuChoiceEditorState = {
    modificationState: modification
    nameInputValue: string
    nameInputErrors: IError[],
    parentInputValue: MenuChoice | null,
    resourceInputValues: Resource[]
}

class MenuChoiceEditor extends React.Component<MenuChoiceEditorProps, MenuChoiceEditorState>{

    constructor(props: MenuChoiceEditorProps) {
        super(props);

        this.state = {
            modificationState: this.props.choiceBeingEdited ? modification.editing : modification.inactive,
            nameInputValue: (this.props.choiceBeingEdited?.name === undefined) ? "" : this.props.choiceBeingEdited.name,
            nameInputErrors: [],
            parentInputValue: (this.props.choiceBeingEdited !== null && this.props.choiceBeingEdited.parentId !== null) ? cache.getMenuChoiceFromId(this.props.choiceBeingEdited.parentId) : null,
            resourceInputValues: (this.props.choiceBeingEdited !== null) ? cache.getResourcesFromIds(this.props.choiceBeingEdited.resourceIds) : [],
        }
    }


    async saveModifiedChoice(updateId: number | null) {
        let newChoice: MenuChoiceDTO = {
            name: this.state.nameInputValue,
            parentId: this.state.parentInputValue?.id ?? null,
            resourceIds: this.state.resourceInputValues.map(resource => resource.id)
        }

        if(newChoice.name === ""){
            this.setState({
                nameInputErrors: [{type: "error", message: "Name cannot be blank"}]
            })
            return;
        }

        this.setState({
            nameInputErrors: []
        })

        if(updateId){
            await api.updateMenuChoice(updateId, newChoice);
        }
        else{
            await api.addMenuChoice(newChoice);
        }


        this.cancelFunc();
        this.props.saveCallback();
    }

    async deleteChoice(deleteId: number){
        if(cache.getMenuChoiceChildrenFromId(this.props.choiceBeingEdited!.id).length > 0){
            if(!window.confirm("Warning:\nDeleting this menu choice will result in all of its children being deleted " +
                "as well.\n\nDelete anyway?")){
                return;
            }
        }

        await api.deleteMenuChoice(deleteId, true);


        this.props.deactivateCallback();
        this.props.saveCallback();
    }

    cancelFunc() {
        this.setState({
            modificationState: modification.inactive,
            nameInputValue: "",
            parentInputValue: null,
            resourceInputValues: []
        })

        this.props.deactivateCallback();
    }

    getParentOptions() : Option[] {
        let options: Option[] = [];

        for(const choice of cache.getAllMenuChoices()){
            if(choice.id !== this.props.choiceBeingEdited?.id) {
                options.push({
                    label: choice.name,
                    value: choice
                })
            }
        }

        return options;
    }

    getResourceOptions() : Option[] {
        let options: Option[] = [];

        for(const resource of cache.getAllResources()){
            let addResource = true;
            for(const existResource of this.state.resourceInputValues){
                if(resource.name === existResource.name){
                    addResource = false;
                    break;
                }
            }

            if(addResource){
                options.push({
                    label: resource.name,
                    value: resource
                })
            }
        }

        return options;
    }

    getCurrentlyEditingText(){
        switch(this.state.modificationState){
            case modification.adding:
                return "ADDING NEW CHOICE"
            case modification.editing:
                return this.props.choiceBeingEdited?.name
            case modification.inactive:
                return "";
        }
    }

    render() {
        return (
          <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
              <Heading>
                  Menu Editor:
              </Heading>
              <p>
                  Currently Editing: <span style={{color: "#3498DB"}}>{
                     this.getCurrentlyEditingText()}</span>
              </p>

              <Row>
                  <Button disabled={this.state.modificationState === modification.adding || this.state.modificationState === modification.editing}
                          onClick={() => {
                              this.setState({
                                  modificationState: modification.adding
                              })
                          }}
                  >
                      Add New Choice
                  </Button>
                  <Button color={"red"}
                          disabled={!(this.state.modificationState === modification.editing)}
                          onClick={() => {
                              this.deleteChoice(this.props.choiceBeingEdited!.id)
                          }}
                  >
                      Delete This Choice
                  </Button>
              </Row>

              <Row width={"60%"} margin={"10px"}>
                  <TextInput label={"Name"}
                             placeholder={"Required"}
                             disabled={this.state.modificationState === modification.inactive}
                             value={this.state.nameInputValue}
                             errors={this.state.nameInputErrors}
                             onChange={newValue => {
                                 this.setState({nameInputValue: newValue})
                             }}
                  />
              </Row>

              <Row width={"60%"} margin={"10px"}  flexAlign={"center"}>
                  <Select autoComplete
                          placeholder={"Optional"}
                          options={this.getParentOptions()}
                          disabled={this.state.modificationState === modification.inactive}
                          value={this.state.parentInputValue}
                          label={"Parent Name"}
                          onChange={(newValue: MenuChoice | null) => {
                              this.setState({
                                  parentInputValue: newValue
                              })
                          }}
                  />

                  <Button color={"orange"}
                          disabled={this.state.modificationState === modification.inactive}
                          onClick={() => {
                              this.setState({
                                  parentInputValue: null
                              })
                          }}
                  >
                      Clear
                  </Button>
              </Row>

              <Row width={"60%"} margin={"10px"}>
                  <Select autoComplete
                          placeholder={"Select to Add"}
                          disabled={this.state.modificationState === modification.inactive}
                          label={"Add Resource"}
                          options={this.getResourceOptions()}
                          onChange={(newValue: Resource) => {
                              this.setState(prevState => ({
                                  resourceInputValues: [...prevState.resourceInputValues, newValue]
                              }))
                          }}
                  />
              </Row>

              <Row>
                  Resources:
                  {this.state.resourceInputValues.map((resource) => (
                      <Button
                          key={resource.id}
                          small={true}
                          color={"gray"}
                          onClick={() => {
                              this.setState({
                                  resourceInputValues: this.state.resourceInputValues.filter(existResource => resource.id !== existResource.id )
                              })
                          }}
                      >
                          {resource.name}
                      </Button>
                  ))}

              </Row>

              <Row flexJustify={"flex-end"} width={"100%"}>
                  <Button color={"green"} disabled={this.state.modificationState === modification.inactive} onClick={() => {
                      if(this.state.modificationState === modification.editing){
                          this.saveModifiedChoice(this.props.choiceBeingEdited!.id)
                      }
                      else{
                          this.saveModifiedChoice(null);
                      }
                  }}>
                      Save
                  </Button>
                  <Button color={"red"} disabled={this.state.modificationState === modification.inactive} onClick={() => {
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