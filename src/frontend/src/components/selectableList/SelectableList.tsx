import React from "react";
import {Button, Row, Search, Table, TBody, Td, TdHeader, TextInput, THead, Tr} from "@foreflight/ffui";

type SelectableListProps<T> = {
    columnNames: string[]
    items: T[];
    getIdFunc: (item: T) => number;
    selectedItemId: number | null;
    selectCallback: (item: T) => void;
    // Called when the value being searched for changes, can be used to update the items prop
    searchCallback: (searchVal: string) => void;
    columnFuncs: {(item: T) : JSX.Element;}[];
}

type SelectableListState = {
    searchValue: string;
}

class SelectableList<T> extends React.Component<SelectableListProps<T>, SelectableListState>{
    constructor(props: SelectableListProps<T>) {
        super(props);

        this.state = {
            searchValue: ""
        }
    }

    render() {
        return (
            <>
                <Row width={"35%"} flexDirection={"column"}>
                    <TextInput value="" placeholder="Search"
                               onChange={newValue => {
                                   this.props.searchCallback(newValue);
                                   this.setState({searchValue: newValue})
                               }}
                    />

                    <Table striped={true}>
                        <THead>
                            <Tr>
                                <TdHeader defaultWidth={"5px"}></TdHeader>
                                {this.props.columnNames.map(column => (
                                    <TdHeader key={column}>{column}</TdHeader>
                                ))}
                            </Tr>
                        </THead>
                        <TBody>
                            {this.props.items.map((item) => (
                                <Tr key={this.props.getIdFunc(item)}>
                                    <Td>
                                        <Button small color={this.props.selectedItemId === this.props.getIdFunc(item) ? "green" : "blue"} onClick={() => {
                                            this.props.selectCallback(item);
                                        }}>
                                            Select
                                        </Button>
                                    </Td>

                                    {this.props.columnFuncs.map((columnFunc, idx) => (
                                        <Td key={idx}>
                                            {columnFunc(item)}
                                        </Td>
                                    ))}
                                </Tr>
                            ))}
                        </TBody>
                    </Table>
                </Row>
            </>
        );
    }
}

export default SelectableList;