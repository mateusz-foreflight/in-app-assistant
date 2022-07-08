import React from "react";
import Source from "../../types/Source";
import {Button, Heading, IError, Row, TextInput} from "@foreflight/ffui";
import {api} from "../../client";
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
    nameInputErrors: IError[]
    linkInputValue: string
    linkInputErrors: IError[]
}

class SourceEditor extends React.Component<SourceEditorProps, SourceEditorState>{
    constructor(props: SourceEditorProps) {
        super(props);

        this.state = {
            modificationState: this.props.sourceBeingEdited ? modification.editing : modification.inactive,
            nameInputValue: this.props.sourceBeingEdited ? this.props.sourceBeingEdited.name : "",
            linkInputValue: this.props.sourceBeingEdited ? this.props.sourceBeingEdited.link : "",
            nameInputErrors: [],
            linkInputErrors: []
        }
    }

    async deleteSource(deleteId: number){
        let deleteFailed = false;
        await api.deleteSource(deleteId, false).catch(() => deleteFailed = true);

        if(deleteFailed){
            if(window.confirm("Warning:\nDeleting this source will result in all resources that use it being removed " +
                "as well.\n\nDelete anyway?")){
                await api.deleteSource(deleteId, true);
            }
            else{
                return;
            }
        }

        this.cancelFunc();
        this.props.saveCallback();
    }

    async saveModifiedSource(updateId: number | null){
        let newSource: SourceDTO = {
            name: this.state.nameInputValue,
            link: this.state.linkInputValue
        }

        // Check for input errors
        let errorOccurred: boolean = false;

        if(newSource.name === ""){
            this.setState({
                nameInputErrors: [{type: "error", message: "Name cannot be blank"}]
            })
            errorOccurred = true;
        }
        else{
            this.setState({
                nameInputErrors: []
            })
        }

        if(newSource.link === ""){
            this.setState({
                linkInputErrors: [{type: "error", message: "Link cannot be blank"}]
            })
            errorOccurred = true;
        }
        else{
            this.setState({
                linkInputErrors: []
            })
        }

        if(errorOccurred){
            return;
        }

        if(updateId){
            await api.updateSource(updateId, newSource);
        }
        else{
            await api.addSource(newSource);
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

    getCurrentlyEditingText(){
        switch(this.state.modificationState){
            case modification.adding:
                return "ADDING NEW CHOICE"
            case modification.editing:
                return this.props.sourceBeingEdited?.name
            case modification.inactive:
                return "";
        }
    }

    render() {
        return (
            <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
                <Heading>
                    Source Editor:
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
                                this.deleteSource(this.props.sourceBeingEdited!.id)
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