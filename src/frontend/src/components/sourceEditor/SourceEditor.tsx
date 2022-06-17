import React from "react";
import Source from "../../types/Source";
import {Button, Heading, Row, TextInput} from "@foreflight/ffui";
import {addResource, addSource, deleteSource, updateResource, updateSource} from "../../client";
import SourceDTO from "../../types/SourceDTO";

enum modification {
    editing,
    adding,
    inactive
}

type SourceEditorProps = {
    sourceBeingEdited: Source | null,
    deactivateCallback: () => void,
    saveCallback: () => void;
}

type SourceEditorState = {
    modificationState: modification
    nameInputValue: string
    linkInputValue: string
}

class SourceEditor extends React.Component<SourceEditorProps, SourceEditorState>{
    constructor(props: SourceEditorProps) {
        super(props);

        this.state = {
            modificationState: this.props.sourceBeingEdited ? modification.editing : modification.inactive,
            nameInputValue: this.props.sourceBeingEdited ? this.props.sourceBeingEdited.name : "",
            linkInputValue: this.props.sourceBeingEdited ? this.props.sourceBeingEdited.link : ""
        }
    }

    async deleteSource(deleteId: number){
        await deleteSource(deleteId);

        this.cancelFunc();
        this.props.saveCallback();
    }

    async saveModifiedSource(updateId: number | null){
        let newSource: SourceDTO = {
            name: this.state.nameInputValue,
            link: this.state.linkInputValue
        }

        if(newSource.name === ""){
            return;
        }
        if(newSource.link === ""){
            return;
        }

        if(updateId){
            await updateSource(updateId, newSource);
        }
        else{
            await addSource(newSource);
        }

        this.cancelFunc();
        this.props.saveCallback();
    }

    cancelFunc(){
        this.setState({
            modificationState: modification.inactive,
            nameInputValue: "",
            linkInputValue: ""
        })

        this.props.deactivateCallback();
    }

    render() {
        return (
            <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
                <Heading>
                    Source Editor:
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
                                this.deleteSource(this.props.sourceBeingEdited!.id)
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

                <Row flexJustify={"flex-end"} width={"100%"}>
                    <Button color={"green"} disabled={this.state.modificationState === modification.inactive} onClick={() => {
                        if(this.state.modificationState === modification.editing){
                            this.saveModifiedSource(this.props.sourceBeingEdited!.id)
                        }
                        else{
                            this.saveModifiedSource(null);
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

export default SourceEditor;