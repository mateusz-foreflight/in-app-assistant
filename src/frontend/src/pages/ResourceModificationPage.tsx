import React from "react";
import ResourceList from "../components/resource/ResourceList";
import Resource from "../types/Resource";
import ResourceEditor from "../components/resource/ResourceEditor";
import Navbar from "../components/common/Navbar";
import {cache} from "../components/common/Cache";

type ResourceModificationPageState = {
    //resources: Resource[];
    // sources: Source[];
    selectedResource: Resource | null;
    searchVal: string;
    displayedResources: Resource[]
}

class ResourceModificationPage extends React.Component<{}, ResourceModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            // resources: [],
            // sources: [],
            selectedResource: null,
            searchVal: "",
            displayedResources: []
        }
    }

    updateResources = async () => {
        await cache.updateResources();
        this.updateDisplayedResources();
    }

    async updateSources() {
        await cache.updateSources();
    }

    updateSelectedResource = (selResource: Resource) => {
        this.setState({
            selectedResource: selResource
        });
    }

    updateDisplayedResources() {
        let displayResources: Resource[] = [];

        for(const resource of cache.getAllResources()){
            if(resource.name.search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayResources.push(resource);
            }
        }

        this.setState({
            displayedResources: displayResources
        })
    }

    async componentDidMount() {
        await this.updateSources();
        await this.updateResources();
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<ResourceModificationPageState>) {
        if(this.state.searchVal !== prevState.searchVal){
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