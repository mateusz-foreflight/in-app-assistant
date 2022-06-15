import React from "react";
import ResourceList from "../components/resourceList/ResourceList";
import Resource from "../types/Resource";
import {getAllResources} from "../client";

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

    async updateResources() {
        let fetchedResources: Resource[] = []

        await getAllResources().then(response => {
            return response.json() as Promise<any[]>;
        }).then(data => {
            data.forEach((resource) => {
                fetchedResources.push(resource as Resource)
            })
        });

        this.setState({
            resources: fetchedResources
        })
    }

    updateSelectedResource = (selResource: Resource) => {
        this.setState({
            selectedResource: selResource
        });
    }

    async componentDidMount() {
        await this.updateResources();
    }

    render() {
        return (
            <div>
                Resource Modification:
                <ResourceList
                    resources={this.state.resources}
                    selectCallback={this.updateSelectedResource}
                    selectedResourceId={this.state.selectedResource ? this.state.selectedResource.id : null}/>
            </div>
        );
    }
}

export default ResourceModificationPage;