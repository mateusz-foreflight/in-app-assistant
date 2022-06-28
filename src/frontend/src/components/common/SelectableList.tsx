import React from "react";
import {
    Button,
    IColumn, IRow, IRowElement, IRowElementObject,
    Row,
    Search,
    SortableTable,
    Table,
    TBody,
    Td,
    TdHeader,
    TextInput,
    THead,
    Tr
} from "@foreflight/ffui";

type SelectableListProps<T> = {
    columnNames: string[]
    items: T[];
    getIdFunc: (item: T) => number;
    selectedItemId: number | null;
    selectCallback: (item: T) => void;
    // Called when the value being searched for changes, can be used to update the items prop
    searchCallback: (searchVal: string) => void;
    columnFuncs: {(item: T) : IRowElement}[];
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

    columnNamesToColumns(names: string[]) : IColumn[] {
        let cols: IColumn[] = [];
        cols.push({name: ""}) // For select button

        for(const name of names){
            cols.push({name: name});
        }
        return cols;
    }

    itemsToRows(items: T[]) : IRow[] {
        let rows: IRow[] = [];

        items.forEach((item) => {
            let row: IRow = [];

            // Add select button to each row
            row.push({raw:
                <Button
                    small
                    color={this.props.selectedItemId === this.props.getIdFunc(item) ? "green" : "blue"}
                    onClick={() => this.props.selectCallback(item)}
                >
                    Select
                </Button>
            })

            this.props.columnFuncs.forEach((func) => {
                row.push(func(item));
            })

            rows.push(row);
        })
        return rows;
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

                    <SortableTable
                        tableRows={this.itemsToRows(this.props.items)}
                        tableColumns={this.columnNamesToColumns(this.props.columnNames)}
                    />
                </Row>
            </>
        );
    }
}

export default SelectableList;