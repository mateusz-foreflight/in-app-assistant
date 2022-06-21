import React from "react";
import {Heading} from "@foreflight/ffui";
import SelectableList from "../selectableList/SelectableList";
import Resource from "../../types/Resource";

type ResourceListProps = {
    resources: Resource[];
    selectCallback: (resource: Resource) => void;
    searchCallback: (searchVal: string) => void;
    selectedResourceId: number | null;
}

type ResourceListState = {
    searchedResources: Resource[];
}

class ResourceList extends React.Component<ResourceListProps, {}>{

    render() {
        return (
          <>
              <Heading>
                  Resource List:
              </Heading>

              <SelectableList<Resource>
                  columnNames={["Resource Name", "Link", "Source"]}
                  items={this.props.resources}
                  getIdFunc={resource => resource.id}
                  selectedItemId={this.props.selectedResourceId}
                  selectCallback={this.props.selectCallback}
                  searchCallback={this.props.searchCallback}
                  columnFuncs={[
                      resource => <>{resource.name}</>,
                      resource => <>{resource.link}</>,
                      resource => <>{resource.source.name}</>
                  ]}
              />
          </>
        );
    }
}

export default ResourceList;