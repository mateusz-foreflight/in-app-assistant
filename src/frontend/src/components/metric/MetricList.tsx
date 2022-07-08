import React from "react";
import SelectableList from "../common/SelectableList";
import Metric from "../../types/Metric";

type MetricListProps = {
    metrics: Metric[]
    selectCallback: (metric: Metric) => void;
    searchCallback: (searchVal: string) => void;
    selectedMetricId: number | null
}

class MetricList extends React.Component<MetricListProps, {}> {

    render() {
        return (
            <div>
                <SelectableList
                    columnNames={["Answer Found", "Timestamp", "Ticket Link", "User Name"]}
                    items={this.props.metrics}
                    getIdFunc={metric => metric.id}
                    selectedItemId={this.props.selectedMetricId}
                    selectCallback={this.props.selectCallback}
                    searchCallback={this.props.searchCallback}
                    columnFuncs={[
                        metric => {return {raw: <div>{metric.answerFound ? "Yes" : "NO"}</div>}},
                        metric => new Date(metric.timestamp ?? "").toLocaleString(),
                        metric => metric.ticketLink ?? "",
                        metric => metric.userName ?? ""
                    ]}
                />
            </div>
        );
    }
}

export default MetricList;