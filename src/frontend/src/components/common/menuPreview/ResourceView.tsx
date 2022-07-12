import React from "react";
import {Button, IColumn, IRow, Row, SortableTable} from "@foreflight/ffui";
import Resource from "../../../types/Resource";
import {cache} from "../Cache";
import Source from "../../../types/Source";

type ResourceViewProps = {
    resources: Resource[];
    visitResourceCallback?: (resource: Resource) => void;
}


class ResourceView extends React.Component<ResourceViewProps, {}>{

    resourceCols: IColumn[] = [
        { name: ""},
        { name: 'Resource Name', defaultWidth:'10%'},
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
            let source: Source = cache.getSourceFromId(resource.sourceId)!;

            newRow.push(resource.name);
            newRow.push({raw:<a href={source.link} target="_blank" rel="noreferrer">{source.name}</a>});
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