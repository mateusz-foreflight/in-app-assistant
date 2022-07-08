import React from "react";
import Metric from "../../types/Metric";
import {Heading, Row, Textarea, TextInput} from "@foreflight/ffui";
import {cache} from "../common/Cache";

type MetricViewPanelProps = {
    metric: Metric | null
}

class MetricViewPanel extends React.Component<MetricViewPanelProps, {}>{

    render() {
        return (
            <Row borderBottom={true} borderTop={true} width={"35%"} margin={"10px"} flexDirection={"column"}>
                <Heading>Metric Info:</Heading>

                <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                    <Heading h3>User Name:</Heading>
                    <Row width={"100%"} style={{"margin-top": "-15px"}}>
                        <TextInput
                            value={this.props.metric ? this.props.metric.userName ?? "" : ""}
                            readOnly
                            placeholder={""}
                        />
                    </Row>
                </Row>

                <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                    <Heading h3>Ticket Link:</Heading>
                    <Row width={"100%"} style={{"margin-top": "-15px"}}>
                        <TextInput
                            value={this.props.metric ? this.props.metric.ticketLink ?? "" : ""}
                            readOnly
                            placeholder={""}
                        />
                    </Row>
                </Row>

                <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                    <Heading h3>Timestamp:</Heading>
                    <Row width={"100%"} style={{"margin-top": "-15px"}}>
                        <TextInput
                            value={this.props.metric ? new Date(this.props.metric.timestamp ?? "").toLocaleString() : ""}
                            readOnly
                            placeholder={""}
                        />
                    </Row>
                </Row>

                {(this.props.metric && !this.props.metric.answerFound) &&
                    <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                        <Heading h3>Feedback:</Heading>
                        <Row width={"90%"} style={{"margin-top": "-30px"}}>
                            <Textarea readOnly
                                      placeholder={"None provided"}
                                      minHeight={"100px"}
                                      disabled={this.props.metric === null}
                                      value={this.props.metric ? this.props.metric.userFeedback ?? "" : ""}
                            />
                        </Row>
                    </Row>
                }

                <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                    <Heading h3>Visited Menu Choices:</Heading>
                    <Row flexDirection={"Column"}>
                        {this.props.metric ? cache.getMenuChoicesFromIds(this.props.metric.menuchoiceIds).map(choice => (
                            <div>{choice.name}</div>
                        )) : ""}
                    </Row>
                </Row>

                <Row width={"100%"} flexDirection={"column"} margin={"10px"}>
                    <Heading h3>Visited Resources:</Heading>
                    <Row flexDirection={"Column"}>
                        {this.props.metric ? cache.getResourcesFromIds(this.props.metric.resourceIds).map(resource => (
                            <a href={resource.link} target="_blank" rel="noreferrer">{resource.name}</a>
                        )) : ""}
                    </Row>
                </Row>
            </Row>
        );
    }
}

export default MetricViewPanel;