import React from "react";
import ResourceList from "../components/resourceList/ResourceList";
import Resource from "../types/Resource";
import {extract, getAllResources} from "../client";
import ResourceEditor from "../components/resourceEditor/ResourceEditor";

type ResourceModificationPageState = {
    resources: Resource[];
    selectedResource: Resource | null;
}

class ResourceModificationPage extends React.Component<{}, ResourceModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            resources: [],
            selectedResource: null
        }
    }

    updateResources = async () => {
        this.setState({
            resources: await extract<Resource>(getAllResources())
        });
    }

    updateSelectedResource = (selResource: Resource) => {
        this.setState({
            selectedResource: selResource
        });
    }

    async componentDidMount() {
        await this.updateResources();
    }

    deselectResource = () => {
        this.setState({
            selectedResource: null
        })
    }

    render() {
        return (
            <div>
                Resource Modification:
                <ResourceEditor
                    key={this.state.selectedResource?.id}
                    resourceBeingEdited={this.state.selectedResource}
                    deactivateCallback={this.deselectResource}
                    saveCallback={this.updateResources}
                />
                <ResourceList
                    resources={this.state.resources}
                    selectCallback={this.updateSelectedResource}
                    selectedResourceId={this.state.selectedResource ? this.state.selectedResource.id : null}
                />
            </div>
        );
    }
}

export default ResourceModificationPage;