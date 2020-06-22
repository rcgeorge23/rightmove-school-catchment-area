<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri='http://java.sun.com/jsp/jstl/core' %>
<html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js" integrity="sha384-qlmct0AOBiA2VPZkMY3+2WqkHtIQ9lSdAsAn5RUJD/3vA5MKDgSGcdmIv4ycVxyn" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/jquery.validate.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/additional-methods.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
    <script src="https:////cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.15.4/css/ui.jqgrid.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.15.4/jquery.jqgrid.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.15.4/i18n/grid.locale-en.js"></script>

    <link rel="stylesheet" href="/css/lcag.css">
    <script src="/js/lcag-common.js"></script>

    <title>Right Move Graveney School Catchment Area Search</title>
</head>
    <body>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a href="#" class="navbar-brand">
                        <img alt="Brand" src="/images/rightmove.png" width="60">
                    </a>
                </div>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12 col-sm-12">
                    <div id="searchForm">
                        <form action="/search" method="post" id="search-form">
                            <div class="panel panel-default">
                                <div class="panel-heading">Search</div>
                                <div class="panel-body">
                                    <div class="form-group">
                                        <label for="minimumPrice">Minimum amount:</label>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon">£</div>
                                                <input type="text" name="minimumPrice" class="form-control" id="minimumPrice" placeholder="Please enter the minimum house price" required value="650000"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="maximumPrice">Maximum amount:</label>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon">£</div>
                                                <input type="text" name="maximumPrice" class="form-control" id="maximumPrice" placeholder="Please enter the maximum house price" required value="1250000"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="minimumNumberOfBedrooms">Minimum number of bedrooms:</label>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon"><i class="fa fa-home" aria-hidden="true"></i></div>
                                                <input type="text" name="minimumNumberOfBedrooms" class="form-control" id="minimumNumberOfBedrooms" placeholder="Please enter the maximum number of bedrooms" required value="3"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="maximumNumberOfBedrooms">Maximum number of bedrooms:</label>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon"><i class="fa fa-home" aria-hidden="true"></i></div>
                                                <input type="text" name="maximumNumberOfBedrooms" class="form-control" id="maximumNumberOfBedrooms" placeholder="Please enter the maximum number of bedrooms" required value="6"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="maximumDistanceToGraveneySchool">Maximum distance to graveney school (m):</label>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon"><i class="fa fa-road" aria-hidden="true"></i></div>
                                                <input type="text" name="maximumDistanceToGraveneySchool" class="form-control" id="maximumDistanceToGraveneySchool" placeholder="Please enter the maximum distance to Graveney School in meters" required value="500"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-footer clearfix">
                                    <button type="submit" class="btn btn-primary btn-block" id="submitButton">Search</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


