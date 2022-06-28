import React from "react";
import {Button, IColumn, IRow, Row, SortableTable} from "@foreflight/ffui";
import Resource from "../../../types/Resource";

type ResourceViewProps = {
    resources: Resource[];
    visitResourceCallback?: (resource: Resource) => void;
}


class ResourceView extends React.Component<ResourceViewProps, {}>{

    resourceCols: IColumn[] = [
        { name: ""},
        { name: 'Resource Name', defaultWidth:'10%'},
        { name: 'Link', defaultWidth:'100px'},
        { name: 'Source', defaultWidth:'7%px'},
    ];


    resourcesToRows(resources: Resource[]){
        let newRows: IRow[] = [];
        resources.forEach((resource) =>{
            let newRow: IRow = [];
            newRow.push({raw:
                    <Button
                        small
                        onClick={() => {
                            window.open(resource.link);
                            if(this.props.visitResourceCallback) {
                                this.props.visitResourceCallback(resource);
                            }
                        }}
                    >
                        Go To
                    </Button>
            })
            newRow.push(resource.name);
            newRow.push(resource.link);
            newRow.push(resource.source.name);
            newRows.push(newRow);
        })

        return newRows;
    }

    render(){
        return(
            <Row margin={"10px"}>
                <SortableTable
                    tableRows={this.resourcesToRows(this.props.resources)}
                    tableColumns={this.resourceCols}/>
            </Row>
        )
    }
}

export default ResourceView;