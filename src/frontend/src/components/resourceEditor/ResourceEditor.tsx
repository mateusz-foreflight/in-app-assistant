import React from "react";
import Resource from "../../types/Resource";
import {Button, Heading, IError, Option, Row, Select, TextInput} from "@foreflight/ffui";
import {addResource, deleteResource, updateResource} from "../../client";
import ResourceDTO from "../../types/ResourceDTO";
import Source from "../../types/Source";

enum modification {
    editing,
    adding,
    inactive
}

type ResourceEditorProps = {
    resourceBeingEdited: Resource | null
    deactivateCallback: () => void;
    saveCallback: () => void;
    allSources: Source[]
}

type ResourceEditorState = {
    modificationState: modification
    nameInputValue: string
    nameInputErrors: IError[]
    linkInputValue: string
    linkInputErrors: IError[]
    sourceInputValue: string
    sourceInputErrors: IError[]
}

class ResourceEditor extends React.Component<ResourceEditorProps, ResourceEditorState>{
    constructor(props: ResourceEditorProps) {
        super(props);

        this.state = {
            modificationState: this.props.resourceBeingEdited ? modification.editing : modification.inactive,
            nameInputValue: (this.props.resourceBeingEdited?.name === undefined) ? "" : this.props.resourceBeingEdited.name,
            linkInputValue: (this.props.resourceBeingEdited?.link === undefined) ? "" : this.props.resourceBeingEdited.link,
            sourceInputValue: (this.props.resourceBeingEdited?.source === undefined) ? "" : this.props.resourceBeingEdited.source.name,
            nameInputErrors: [],
            linkInputErrors: [],
            sourceInputErrors: []
        }
    }

    async deleteResource(deleteId: number){
        let deleteFailed = false;
        await deleteResource(deleteId, false).catch(() => deleteFailed = true);

        if(deleteFailed){
            if(window.confirm("Warning:\nDeleting this resource will result in it being removed from all menu " +
                "choices that use it.\n\nDelete anyway?")){
                await deleteResource(deleteId, true)
            }
            else{
                return;
            }
        }

        this.cancelFunc();
        this.props.saveCallback();
    }

    async saveModifiedResource(updateId: number | null) {
        let newResource: ResourceDTO = {
            name: this.state.nameInputValue,
            link: this.state.linkInputValue,
            source: this.state.sourceInputValue
        }

        // Check for input errors
        let errorOccured = false;

        if(newResource.name === ""){
            this.setState({
                nameInputErrors: [{type: "error", message: "Name cannot be blank"}]
            })
            errorOccured = true;
        }
        else{
            this.setState({
                nameInputErrors: []
            })
        }

        if(newResource.link === ""){
            this.setState({
                linkInputErrors: [{type: "error", message: "Link cannot be blank"}]
            })
            errorOccured = true;
        }
        else{
            this.setState({
                linkInputErrors: []
            })
        }

        if(newResource.source === ""){
            this.setState({
                sourceInputErrors: [{type: "error", message: "Source cannot be blank"}]
            })
            errorOccured = true;
        }
        else{
            this.setState({
                sourceInputErrors: []
            })
        }

        if(errorOccured){
            return;
        }

        if(updateId){
            await updateResource(updateId, newResource);
        }
        else{
            await addResource(newResource);
        }

        this.cancelFunc();
        this.props.saveCallback();
    }

    getSourceOptions() : Option[] {
        let options: Option[] = [];

        for(const source of this.props.allSources){
            options.push({
                label: source.name,
                value: source.name
            });
        }

        return options;
    }

    cancelFunc() {
        this.setState({
            modificationState: modification.inactive,
            nameInputValue: "",
            linkInputValue: "",
            sourceInputValue: ""
        })

        this.props.deactivateCallback()
    }

    getCurrentlyEditingText(){
        switch(this.state.modificationState){
            case modification.adding:
                return "ADDING NEW CHOICE"
            case modification.editing:
                return this.props.resourceBeingEdited?.name
            case modification.inactive:
                return "";
        }
    }

    render() {
        return (
            <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
                <Heading>
                    Resource Editor:
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
                                this.deleteResource(this.props.resourceBeingEdited!.id)
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

                <Row width={"60%"} margin={"10px"}>
                    <TextInput label={"Link"}
                               placeholder={"Required"}
                               disabled={this.state.modificationState === modification.inactive}
                               value={this.state.linkInputValue}
                               errors={this.state.linkInputErrors}
                               onChange={newValue => {
                                   this.setState({linkInputValue: newValue})
                               }}
                    />
                </Row>

                <Row width={"60%"} margin={"10px"}>
                    <Select autoComplete
                            placeholder={"Required"}
                            options={this.getSourceOptions()}
                            disabled={this.state.modificationState === modification.inactive}
                            value={this.state.sourceInputValue}
                            label={"Source"}
                            errors={this.state.sourceInputErrors}
                            onChange={(newValue: string) => {
                                this.setState({
                                    sourceInputValue: newValue
                                })
                            }}
                    />
                </Row>

                <Row flexJustify={"flex-end"} width={"100%"}>
                    <Button color={"green"} disabled={this.state.modificationState === modification.inactive} onClick={() => {
                        if(this.state.modificationState === modification.editing){
                            this.saveModifiedResource(this.props.resourceBeingEdited!.id)
                        }
                        else{
                            this.saveModifiedResource(null);
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

export default ResourceEditor;