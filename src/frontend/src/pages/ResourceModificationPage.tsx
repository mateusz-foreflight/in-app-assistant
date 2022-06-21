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
    searchVal: string;
    displayedResources: Resource[]
}

class ResourceModificationPage extends React.Component<{}, ResourceModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            resources: [],
            sources: [],
            selectedResource: null,
            searchVal: "",
            displayedResources: []
        }
    }

    updateResources = async () => {
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

    updateDisplayedResources() {
        let displayResources: Resource[] = [];

        for(const resource of this.state.resources){
            if(resource.name.search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayResources.push(resource);
            }
        }

        this.setState({
            displayedResources: displayResources
        })
    }

    async componentDidMount() {
        await this.updateResources();


        await this.updateSources();
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<ResourceModificationPageState>) {
        if(this.state.searchVal !== prevState.searchVal || this.state.resources !== prevState.resources){
            this.updateDisplayedResources();
        }
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
                    resources={this.state.displayedResources}
                    selectCallback={this.updateSelectedResource}
                    searchCallback={searchVal => this.setState({searchVal: searchVal})}
                    selectedResourceId={this.state.selectedResource ? this.state.selectedResource.id : null}
                />
            </div>
        );
    }
}

export default ResourceModificationPage;