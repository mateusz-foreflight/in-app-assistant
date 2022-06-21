import React from "react";
import ResourceList from "../components/resourceList/ResourceList";
import Resource from "../types/Resource";
import {extract, getAllResources, getAllSources} from "../client";
import ResourceEditor from "../components/resourceEditor/ResourceEditor";
import Source from "../types/Source";
import Navbar from "../components/navbar/Navbar";

type ResourceModificationPageState = {
    resources: Resource[];
    sources: Source[];
    selectedResource: Resource | null;
}

class ResourceModificationPage extends React.Component<{}, ResourceModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            resources: [],
            sources: [],
            selectedResource: null
        }
    }

    updateResources = async () => {
        console.log(await extract<Resource>(getAllResources()))

        this.setState({
            resources: await extract<Resource>(getAllResources())
        });
    }

    async updateSources() {
        this.setState({
            sources: await extract<Source>(getAllSources())
        })
    }

    updateSelectedResource = (selResource: Resource) => {
        this.setState({
            selectedResource: selResource
        });
    }

    async componentDidMount() {
        await this.updateResources();


        await this.updateSources();
    }

    deselectResource = () => {
        this.setState({
            selectedResource: null
        })
    }

    render() {
        return (
            <div>
                <Navbar activePage={2}/>

                <ResourceEditor
                    key={this.state.selectedResource?.id}
                    resourceBeingEdited={this.state.selectedResource}
                    deactivateCallback={this.deselectResource}
                    saveCallback={this.updateResources}
                    allSources={this.state.sources}
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