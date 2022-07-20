import React from "react";
import SourceList from "../components/source/SourceList";
import Source from "../types/Source";
import SourceEditor from "../components/source/SourceEditor";
import Navbar from "../components/common/Navbar";
import {cache} from "../components/common/Cache";

type SourceModificationState = {
    //sources: Source[];
    selectedSource: Source | null;
    searchVal: string
    displayedSources: Source[]
}

class SourceModificationPage extends React.Component<{}, SourceModificationState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            //sources: [],
            selectedSource: null,
            searchVal: "",
            displayedSources: []
        }
    }

    updateSources = async () => {
        await cache.updateSources();
        this.updateDisplayedSources();
    }

    updateSelectedSource = (selSource: Source) => {
        this.setState({
            selectedSource: selSource
        })
    }

    updateDisplayedSources() {
        let displayedSources: Source[] = [];

        for(const source of cache.getAllSources()){
            if(source.name.search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayedSources.push(source);
            }
        }

        this.setState({
            displayedSources: displayedSources
        })
    }

    deselectSource = () => {
        this.setState({
            selectedSource: null
        })
    }

    async componentDidMount() {
        await this.updateSources();
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<SourceModificationState>) {
        if(this.state.searchVal !== prevState.searchVal){
            this.updateDisplayedSources();
        }
    }

    render() {
        return (
            <div className={"page"}>
                <Navbar activePage={3}/>

                <SourceEditor
                    key={this.state.selectedSource?.id}
                    sourceBeingEdited={this.state.selectedSource}
                    deactivateCallback={this.deselectSource}
                    saveCallback={this.updateSources}
                />
                <SourceList
                    sources={this.state.displayedSources}
                    selectCallback={this.updateSelectedSource}
                    searchCallback={searchVal => this.setState({searchVal: searchVal})}
                    selectedSourceId={this.state.selectedSource ? this.state.selectedSource.id : null}
                />
            </div>
        );
    }
}

export default SourceModificationPage;