import React from "react";
import MetricList from "../components/metric/MetricList";
import Metric from "../types/Metric";
import Navbar from "../components/common/Navbar";
import MetricViewPanel from "../components/metric/MetricViewPanel";
import {cache} from "../components/common/Cache";

type MetricsPageState = {
    //metrics: Metric[]
    displayedMetrics: Metric[]
    selectedMetric: Metric | null;
    searchVal: string
}

class MetricsPage extends React.Component<{}, MetricsPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            //metrics: [],
            displayedMetrics: [],
            selectedMetric: null,
            searchVal: ""
        }
    }

    updateDisplayedResources() {
        let displayed: Metric[] = [];

        for(const metric of cache.getAllMetrics()){
            if(metric.userName && metric.userName.search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayed.push(metric);
            }
            if(!metric.userName && this.state.searchVal === ""){
                displayed.push(metric);
            }
        }

        this.setState({
            displayedMetrics: displayed
        })
    }

    async componentDidMount() {
        await cache.refresh();
        this.updateDisplayedResources();
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<MetricsPageState>) {
        if(this.state.searchVal !== prevState.searchVal){
            this.updateDisplayedResources();
        }
    }


    render() {
        return (
            <div>
                <Navbar activePage={5}/>

                <MetricViewPanel
                    metric={this.state.selectedMetric}
                    deselectCallback={() => this.setState({selectedMetric: null})}
                />


                <MetricList
                    metrics={this.state.displayedMetrics}
                    selectCallback={selected => this.setState({selectedMetric: selected})}
                    searchCallback={searchVal => this.setState({searchVal: searchVal})}
                    selectedMetricId={this.state.selectedMetric ? this.state.selectedMetric.id : null}
                />
            </div>
        );
    }
}

export default MetricsPage;