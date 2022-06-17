import React from "react";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";
import Resource from "../../types/Resource";
import {Button, Heading, Option, Row, Select, TextInput} from "@foreflight/ffui";
import {addResource, deleteResource, updateResource} from "../../client";
import ResourceDTO from "../../types/ResourceDTO";

enum modification {
    editing,
    adding,
    inactive
}

type ResourceEditorProps = {
    resourceBeingEdited: Resource | null
    deactivateCallback: () => void;
    saveCallback: () => void;
}

type ResourceEditorState = {
    modificationState: modification
    nameInputValue: string
    linkInputValue: string
    sourceInputValue: string
}

class ResourceEditor extends React.Component<ResourceEditorProps, ResourceEditorState>{
    constructor(props: ResourceEditorProps) {
        super(props);

        this.state = {
            modificationState: this.props.resourceBeingEdited ? modification.editing : modification.inactive,
            nameInputValue: (this.props.resourceBeingEdited?.name === undefined) ? "" : this.props.resourceBeingEdited.name,
            linkInputValue: (this.props.resourceBeingEdited?.link === undefined) ? "" : this.props.resourceBeingEdited.link,
            sourceInputValue: (this.props.resourceBeingEdited?.source === undefined) ? "" : this.props.resourceBeingEdited.source,
        }
    }

    async deleteResource(deleteId: number){
        await deleteResource(deleteId, true);

        this.cancelFunc();
        this.props.saveCallback();
    }

    async saveModifiedResource(updateId: number | null) {
        let newResource: ResourceDTO = {
            name: this.state.nameInputValue,
            link: this.state.linkInputValue,
            source: this.state.sourceInputValue
        }

        if(newResource.name === ""){
            return;
        }

        if(newResource.link === ""){
            return;
        }

        if(newResource.source === ""){
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
        return [
            {label: "Jira", value: "Jira"},
            {label: "Pilot Guide", value: "PilotGuide"}
        ]
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

    render() {
        return (
            <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
                <Heading>
                    Resource Editor:
                </Heading>

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
                               disabled={this.state.modificationState === modification.inactive}
                               value={this.state.nameInputValue}
                               onChange={newValue => {
                                   this.setState({nameInputValue: newValue})
                               }}
                    />
                </Row>

                <Row width={"60%"} margin={"10px"}>
                    <TextInput label={"Link"}
                               disabled={this.state.modificationState === modification.inactive}
                               value={this.state.linkInputValue}
                               onChange={newValue => {
                                   this.setState({linkInputValue: newValue})
                               }}
                    />
                </Row>

                <Row width={"60%"} margin={"10px"}>
                    <Select autoComplete
                            options={this.getSourceOptions()}
                            disabled={this.state.modificationState === modification.inactive}
                            value={this.state.sourceInputValue}
                            label={"Source"}
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