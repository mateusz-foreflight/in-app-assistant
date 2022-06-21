import React from "react";
import SourceList from "../components/sourceList/SourceList";
import Source from "../types/Source";
import {extract, getAllSources} from "../client";
import SourceEditor from "../components/sourceEditor/SourceEditor";
import Navbar from "../components/navbar/Navbar";

type SourceModificationState = {
    sources: Source[];
    selectedSource: Source | null;
}

class SourceModificationPage extends React.Component<{}, SourceModificationState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            sources: [],
            selectedSource: null
        }
    }

    updateSources = async () => {
        this.setState({
            sources: await extract<Source>(getAllSources())
        })
    }

    updateSelectedSource = (selSource: Source) => {
        this.setState({
            selectedSource: selSource
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

    render() {
        return (
            <div>
                <Navbar activePage={3}/>

                <SourceEditor
                    key={this.state.selectedSource?.id}
                    sourceBeingEdited={this.state.selectedSource}
                    deactivateCallback={this.deselectSource}
                    saveCallback={this.updateSources}
                />
                <SourceList
                    sources={this.state.sources}
                    selectCallback={this.updateSelectedSource}
                    selectedSourceId={this.state.selectedSource ? this.state.selectedSource.id : null}
                />
            </div>
        );
    }
}

export default SourceModificationPage;