    <%--

    Licensed Materials - Property of IBM
    IBM Product Name: 5725I02
    (c) Copyright IBM Corp. 2005, 2018
    U.S. Government Users Restricted Rights: Use, duplication or disclosure
    restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<c:if test="${PresenterCommand.concertMode}">
    <div class="concertToolbar">
        <div class="concertBtnContainer">
            <div class="concertCreateMetricBtn"></div>
        </div>
    </div>
</c:if>

    <div id="menu">
        <ul id="drop-down-wrap">
        </ul>

    </div>

    <div id=error-container></div>
    <div class=row-container>
        <div class="svg-container">
            <div class="level-selection-container">
                <div class="level-selection-buttons-container"></div>
                <div class = "level-data-range-container">
                    <text class = "level-date-range-text"></text>
                </div>
            </div>
        </div>
        <div class="panel-container"></div>
    </div>
    <div class = "table-container"></div>




<script type="text/javascript">

    var compPlanId = ${PlanID};
    var isModule = ${IsModule};

    var msg = {
       noreport: "<fmt:message key="pulse.no.report"/>",
       emptyself: "<fmt:message key="pulse.empty.self.report"/>",
       emptychild: "<fmt:message key="pulse.empty.child.report"/>",
     };
    jQuery(document).ajaxError(function (xhr, props) {
            if (props.status === 401) {
                location.reload(); 
            }
    });

    jQuery(document).ready(function() {

        d3.select("body").append("div").attr("class","footer-container");

        if(isModule){
            initModuleMenu();
        }			
        else{
            var compPlanMenu = jQuery('tr.planmenu');
            compPlanMenu.hide();
            initSinglReportMenu();
        }
    });
    
    function initModuleMenu() {

        var requestUrl = '../web/api/v1/plans';
        
        jQuery.ajax({
            url: requestUrl,
            type: 'GET',
            dataType: 'json',
            data: {
            },
            traditional: true,
            retries: 3,
            success: function (result) {
                buildCompPlanMenu(result);
            },
            error: function (errorMsg) {
                if (this.retries > 0) {
                    this.retries--;
                    jQuery.ajax(this);
                }
            }
        });
    }
    

    function buildCompPlanMenu(result){
        var plans = result.plans;

        if (plans.length === 0){
            showEmptyResultMsg(msg.noreport);
            return;
        }



        plans.sort(function(a, b) {return a.name.localeCompare(b.name)});

        var reportHeader = d3.select("#drop-down-wrap").append("li").attr("id","plan-header")
            .on("mouseover", function(){
                d3.select("#plan-header").select("a").style("color", "#32c3fd");
                return d3.select("#plan-drop-down").style("display", "block");
            })
            .on("mouseout", function(){
                d3.select("#plan-header").select("a").style("color", "#5a5a5a");
                return d3.select("#plan-drop-down").style("display", "none");
            });

        reportHeader.append("a").attr("href","#").text(plans[0].name);

        var dropDown = reportHeader.append("ul")
                            .attr("id", "plan-drop-down");
        dropDown.selectAll(".report-list-item").data(plans).enter()
                        .append("li")
                            .attr("class", "report-list-item")
                            .attr("id",function(d){return d.id;})
                            .on("click", function(d) {
                                    selectPlan(d);
                                    d3.select("#plan-header").select("a").style("color", "#5a5a5a");
                                    d3.select("#plan-drop-down").style("display", "none");
                            })
                        .append("a")
                            .attr("href","#")
                            .text(function(d){return d.name;});

        selectPlan(plans[0]);
    }


    function selectPlan(plan){
        compPlanId = plan.id;
        d3.select("#plan-header").select("a").text(plan.name);
        resetReport();
        initSinglReportMenu();
    }
    
    function initSinglReportMenu()	{

        var reportMenu = jQuery('select#reportmenu');
        reportMenu.empty();
        var requestUrl = '../web/api/v1/plans/'+ compPlanId +'/reports';


        jQuery.ajax({
            url: requestUrl,
            type: 'GET',
            dataType: 'json',
            data: {
            },
            traditional: true,
            retries: 3,
            success: function (result) {
                buildCompPlanReportMenu(result);
            },
            error: function (errorMsg) {
                if (this.retries > 0) {
                    this.retries--;
                    jQuery.ajax(this);
                }
            }
        });
    }
    


    function buildCompPlanReportMenu(result){
        d3.select("#report-header").remove();

        if (result.reports.length === 0){
            showEmptyResultMsg(msg.noreport);
            return;
        }

        var reportHeader = d3.select("#drop-down-wrap").append("li").attr("id","report-header")
                                .on("mouseover", function(){
                                    d3.select("#report-header").select("a").style("color", "#32c3fd");
                                    return d3.select("#report-drop-down").style("display", "block");
                                })
                                .on("mouseout", function(){
                                    d3.select("#report-header").select("a").style("color", "#5a5a5a");
                                    return d3.select("#report-drop-down").style("display", "none");
                                });

        reportHeader.append("a").attr("href","#").text(getReportDisplayName(result.reports[0]));

        var dropDown = reportHeader.append("ul")
                                        .attr("id", "report-drop-down");
        dropDown.selectAll(".report-list-item").data(result.reports).enter()
                    .append("li")
                        .attr("class", "report-list-item")
                        .attr("id",function(d){return d;})
                        .on("click", function(d) {
                            d3.select("#report-header").select("a").style("color", "#5a5a5a");
                            d3.select("#report-drop-down").style("display", "none");
                            reportSelected(d);
                        })
                    .append("a")
                        .attr("href","#")
                        .text(function(d){return getReportDisplayName(d);});


        reportSelected(result.reports[0]);
    }

    function getReportDisplayName(reportName){
        var displayName = "";
        switch (reportName){
            case "TransactionsAndProductionResults":
                displayName = "Sales Transactions Report";
                break;
            case "CompensationSummary":
                displayName = "My Earnings Report";
                break;
            case "PerformanceReport":
                displayName = "Team Performance Report";
                break;
            case "SalesManagerOverview":
                displayName = "Team Rankings Report";
                break;
            default:
                displayName = "";
        }

        return displayName;
    }
    
    function reportSelected(reportName){
        if(reportName.length == 0){
            return;
        }
        var requestUrl = '../web/api/v1/plans/'+compPlanId+'/reports/'+reportName;
        var calendarUrl = '../web/api/v1/plans/'+ compPlanId +'/calendar';
        var calendarData;
        jQuery.ajax({
        url: calendarUrl,
        type: 'GET',
        dataType: 'json',
        data: {
        },
        traditional: true,
        retries: 3,
        success: function (calendarResult) {
            calendarData = calendarResult
            jQuery.ajax({
            url: requestUrl,
            type: 'GET',
            dataType: 'json',
            data: {
            },
            traditional: true,
            retries: 3,
            success: function (result) {
                showReport(result, reportName, calendarData);
        },
            error: function (errorMsg) {
            if (this.retries > 0) {
            this.retries--;
            jQuery.ajax(this);
            }
            }
            });

        },
        error: function (errorMsg) {
        if (this.retries > 0) {
        this.retries--;
        jQuery.ajax(this);
        }
        else{
            
        }
        }
        });

        }
    
    function showReport(report, reportName, calendarData){

            resetReport();

            d3.select("#report-header").select("a").text(getReportDisplayName(reportName));

            if (isEmptyReport(report)){
                
                if (reportName === "TransactionsAndProductionResults" || reportName === "CompensationSummary")
                    showEmptyResultMsg(msg.emptyself)
                else if (reportName === "SalesManagerOverview" || reportName === "PerformanceReport")
                    showEmptyResultMsg(msg.emptychild)
                
                return;
            }

            if (reportName === "TransactionsAndProductionResults") {
                renderTransactionReport(report, calendarData);
            }
            if (reportName === "CompensationSummary") {
                renderEarningReport(report);
            }
            if (reportName === "SalesManagerOverview") {
                renderRankingReport(report, calendarData);
            }
            if (reportName === "PerformanceReport") {
                renderPerformanceReport(report, calendarData);
            }
    }

    function resetReport(){

        d3.selectAll('#error-container').selectAll("*").remove();
        d3.selectAll('.svg-container').selectAll("*").remove();
        var levelContainer = d3.selectAll('.svg-container').append("div").attr("class","level-selection-container");
        levelContainer.append("div").attr("class","level-selection-buttons-container");
        levelContainer.append("div").attr("class","level-data-range-container")
                                        .append("text").attr("class","level-date-range-text");

        d3.selectAll('.panel-container')
            .style("height",null) // height was changed in performance report to make payee scrollable
            .style("align-items","stretch")
            .style("overflow",null)
            .style("margin-top", null)
            .style("margin-left", null)
            .selectAll("*")
                .remove();
        d3.selectAll('.table-container').selectAll("*").remove();
        d3.selectAll('.table-container')
            .style('margin-bottom', null)
            .style('border', null);
        d3.selectAll('.footer-container').remove();
        d3.select("body").style("height", null);
    }

    function isEmptyReport(report){
        var tableCount = report.categories.length;
        var emptyTableCount = 0;
        report.categories.forEach(function(d){
            if (d.data.length === 0)
                emptyTableCount++;
        });
        return tableCount === emptyTableCount;
    }

    function showEmptyResultMsg(msg){
            d3.select("#error-container").append("text").text(msg)
    }

</script>
<script src="<c:url value="/scripts/jquery-ui.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/compplanmanager/jquery.dataTables.min.js" />" type="text/javascript"></script>
