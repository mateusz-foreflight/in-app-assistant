import React from "react";
import {Button, Heading} from "@foreflight/ffui";
import SelectableList from "../common/SelectableList";
import Resource from "../../types/Resource";
import {cache} from "../common/Cache";
import resource from "../../types/Resource";

type ResourceListProps = {
    resources: Resource[];
    selectCallback: (resource: Resource) => void;
    searchCallback: (searchVal: string) => void;
    selectedResourceId: number | null;
}

class ResourceList extends React.Component<ResourceListProps, {}>{

    render() {
        return (
          <>
              <Heading>
                  Resource List:
              </Heading>

              <SelectableList<Resource>
                  columnNames={["Resource Name", "Public", "Link", "Source"]}
                  items={this.props.resources}
                  getIdFunc={resource => resource.id}
                  selectedItemId={this.props.selectedResourceId}
                  selectCallback={this.props.selectCallback}
                  searchCallback={this.props.searchCallback}
                  columnFuncs={[
                      resource => resource.name,
                      resource => resource.isPublic ? "True" : "False",
                      resource => {return {raw:
                              <Button
                                  small
                                  onClick={() => window.open(resource.link)}
                              >
                                  Go To
                              </Button>}},
                      resource => cache.getSourceFromId(resource.sourceId)!.name
                  ]}
              />
          </>
        );
    }
}

export default ResourceList;