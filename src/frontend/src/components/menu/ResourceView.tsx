import React from "react";
import {IColumn, IRow, SortableTable, Table, TBody, Td, TdHeader, THead, Tr} from "@foreflight/ffui";
import Resource from "../../types/Resource";

type ResourceViewProps = {
    resources: Resource[];
}


class ResourceView extends React.Component<ResourceViewProps, {}>{
    constructor(props: ResourceViewProps) {
        super(props);
    }


    resourceCols: IColumn[] = [
        { name: 'Resource Name', defaultWidth:'10%'},
        { name: 'Link', defaultWidth:'100px'},
        { name: 'Source', defaultWidth:'7%px'},
    ];


    resourcesToRows(resources: Resource[]){
        let newRows: IRow[] = [];
        resources.forEach((resource) =>{
            let newRow: IRow = [];
            newRow.push(resource.name);
            newRow.push(resource.link);
            newRow.push(resource.source);
            newRows.push(newRow);
        })

        return newRows;
    }

    render(){
        return(
            <>
                <SortableTable tableRows={this.resourcesToRows(this.props.resources)} tableColumns={this.resourceCols}/>
            </>
        )
    }
}

export default ResourceView;