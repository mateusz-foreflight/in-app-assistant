import React from "react";
import MetricList from "../components/metric/MetricList";
import Metric from "../types/Metric";
import {extract, getAllMetrics} from "../client";
import Resource from "../types/Resource";
import Navbar from "../components/common/Navbar";
import MetricViewPanel from "../components/metric/MetricViewPanel";
import {Heading} from "@foreflight/ffui";

type MetricsPageState = {
    metrics: Metric[]
    displayedMetrics: Metric[]
    selectedMetric: Metric | null;
    searchVal: string
}

class MetricsPage extends React.Component<{}, MetricsPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            metrics: [],
            displayedMetrics: [],
            selectedMetric: null,
            searchVal: ""
        }
    }

    updateDisplayedResources() {
        let displayed: Metric[] = [];

        for(const metric of this.state.metrics){
            if(metric.userName .search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayed.push(metric);
            }
        }

        this.setState({
            displayedMetrics: displayed
        })
    }

    async componentDidMount() {
        this.setState({
            metrics: await extract<Metric>(getAllMetrics())
        })
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<MetricsPageState>) {
        if(this.state.searchVal !== prevState.searchVal || this.state.metrics !== prevState.metrics){
            this.updateDisplayedResources();
        }
    }


    render() {
        return (
            <div>
                <Navbar activePage={5}/>

                <MetricViewPanel
                    metric={this.state.selectedMetric}
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