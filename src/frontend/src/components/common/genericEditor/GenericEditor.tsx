import React from "react";
import {Button, Row} from "@foreflight/ffui";


export enum modification {
    editing,
    adding,
    inactive
}

type GenericEditorProps = {
    // This denotes what modification the editor is currently doing
    modificationState: modification

    // Called when an item should be deleted
    deleteItemFunc: () => void

    // Called when an item should be updated, or when new item should be added, in which case true will be passed
    saveItemFunc: (add: boolean) => void

    // Called when cancel is pressed and the item needs to be cleared.
    // Should set modificationState to modification.inactive
    cancelCallback: () => void

    // Called when add new item button is clicked
    // Should set modificationState to modification.adding
    addingNewItemCallback: () => void

    fields: {(disabled: boolean) : JSX.Element}[]

    addNewButtonMsg: string
    deleteButtonMsg: string
}


class GenericEditor extends React.Component<GenericEditorProps, {}>{

    constructor(props: GenericEditorProps) {
        super(props);

    }


    render() {
        return (
            <Row>
                <p>
                    Currently Editing:
                </p>

                <Row>
                    <Button disabled={this.props.modificationState === modification.adding|| this.props.modificationState === modification.editing}
                            onClick={() => {
                                this.props.addingNewItemCallback();
                            }}
                    >
                        {this.props.addNewButtonMsg}
                    </Button>
                    <Button color={"red"}
                            disabled={!(this.props.modificationState === modification.editing)}
                            onClick={() =>{
                                this.props.deleteItemFunc();
                            }}
                    >
                        {this.props.deleteButtonMsg}
                    </Button>
                </Row>

                {this.props.fields.map(field => (
                    <Row>
                        {field(this.props.modificationState === modification.inactive)}
                    </Row>
                ))}

                <Row>
                    <Button disabled={this.props.modificationState === modification.inactive}
                            color={"green"}
                            onClick={() => {
                                this.props.saveItemFunc(this.props.modificationState === modification.adding);
                            }}
                    >
                        Save
                    </Button>
                    <Button disabled={this.props.modificationState === modification.inactive}
                            color={"red"}
                            onClick={() =>{
                                this.props.cancelCallback();
                            }}
                    >
                        Cancel
                    </Button>
                </Row>

            </Row>
        )
    }
}

export default GenericEditor;