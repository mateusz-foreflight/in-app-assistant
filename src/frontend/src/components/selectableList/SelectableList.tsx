import React from "react";
import {Button, Heading, Row, Table, TBody, Td, TdHeader, THead, Tr} from "@foreflight/ffui";

type SelectableListProps<T> = {
    columnNames: string[]
    items: T[];
    getIdFunc: (item: T) => number;
    selectedItemId: number | null;
    selectCallback: (item: T) => void;
    columnFuncs: {(item: T) : JSX.Element;}[];
}

class SelectableList<T> extends React.Component<SelectableListProps<T>, { }>{
    constructor(props: SelectableListProps<T>) {
        super(props);
    }


    render() {
        return (
            <>
                <Row width={"35%"}>
                    <Table striped={true}>
                        <THead>
                            <Tr>
                                <TdHeader defaultWidth={"5px"}></TdHeader>
                                {this.props.columnNames.map(column => (
                                    <TdHeader>{column}</TdHeader>
                                ))}
                            </Tr>
                        </THead>
                        <TBody>
                            {this.props.items.map((item) => (
                                <Tr key={this.props.getIdFunc(item)}>
                                    <Td>
                                        <Button small color={this.props.selectedItemId == this.props.getIdFunc(item) ? "green" : "blue"} onClick={() => {
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