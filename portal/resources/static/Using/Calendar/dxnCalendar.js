(function ($) {
    function init(elem) {
        elem.$_leftdiv = $('<div class="aboluo-leftdiv"></div>');
        elem.$_tools = $('<div class="aboluo-tools"></div>');
        elem.$_calendaryear = $('<div class="aboluo-calendar-select-year"></div>');
        elem.$_calendarmonth = $('<div class="aboluo-calendar-month"></div>');
        elem.$_aperv = $('<a class="aboluo-month-a-perv" href="javascript:;">&lt; </a>');
        elem.$_anext = $('<a class="aboluo-month-a-next" href="javascript:;"> &gt;</a>');
        elem.$_inputToToday = $('<input type="button" class="aboluo-toToday" value="返回今天"/>');
        elem.$_rilidiv = $('<div class="aboluo-rilidiv"></div>');
        elem.$_rilitable = $('<table class="aboluo-rilitable" cellspacing="0" cellpadding="0"></table>');
        elem.$_rilithead = $('<thead class="aboluo-rilithead"></thead>');
        elem.$_rilitr = $('<tr><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th style="color:red;">六</th><th style="color:red;">日</th></tr>');
        elem.$_rightdiv = $('<div class="aboluo-rightdiv"></div>');
        elem.$_xssj = $('<p class="aboluo-xssj"></p>');
        elem.$_currday = $('<p class="aboluo-currday"></p>');
        elem.$_ssjjr = $('<p class="aboluo-ssjjr"></p>');
        elem.$_xsmx = $('<p class="aboluo-xsmx"></p>');

        elem.$_calendarmonth.append(elem.$_aperv, elem.$_anext);
        elem.$_tools.append(elem.$_calendaryear, elem.$_calendarmonth, elem.$_inputToToday);
        elem.$_rilithead.append(elem.$_rilitr);
        elem.$_rilitable.append(elem.$_rilithead);
        elem.$_rilidiv.append(elem.$_rilitable);
        elem.$_leftdiv.append(elem.$_tools, elem.$_rilidiv);
        elem.$_rightdiv.append(elem.$_xssj, elem.$_currday, elem.$_ssjjr, elem.$_xsmx);
        elem.append(elem.$_leftdiv, elem.$_rightdiv);
        createSelectYear(elem);
        createMonthSelect(elem);
        //根据年，月，用table绘制日历。 年月变动则 重新绘制
        createTabledate(elem, parseInt(elem.$_yearSelect[0].value), parseInt(elem.$_monthSelect[0].value));
        //上月下月的a标签给事件
        leftrightclick(elem);
        //设置右边显示栏显示内容，显示栏还可以设置节假日的状态等
        setRigth(elem, new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate());
    };

    $.fn.dxnCalendar = function (options, para) {
        if (typeof options == "string") {
            var _4b5 = $.fn.dxnCalendar.methods[options];
            return _4b5(this, para);
        }
        var _87b = $.data(this[0], "dxnCalendar");
        if (_87b) {
            _87b.options = $.extend(_87b.options, options);
        } else {
            var opts = $.extend({}, $.extend({}, $.fn.dxnCalendar.defaults, options));
            $.data(this[0], "dxnCalendar", {
                options: opts
            });
        }
        return init(this);
    };

    $.fn.dxnCalendar.defaults = $.extend({}, $.fn.dxnCalendar.defaults, {
        setHolidayUrl: '',
        onGetHolidays: function (year, month) {

        },
        onsetHolidayUrlError: function (json) {

        }
    });

    $.fn.dxnCalendar.methods = {
        options: function (jq) {

        }
    };

    //定义了yearselect并赋值,且添加事件，选择则对应的table日期也将改变,且已选中日期会跳到当前选择月的日期，然后给右边明细栏赋值
    function createSelectYear(elem) {
        elem.$_yearSelect = $('<select name="aboluo-yearSelect" id="aboluo-yearSelect"></select>');
        elem.$_calendaryear.html(elem.$_yearSelect);
        var nowTime = new Date();
        var currYear = nowTime.getFullYear();
        for (var i = 0; i <= 79; i++) {
            elem.$_yearSelect[0].options.add(new Option((i + 1970) + "年", i + 1970));
            if (currYear == i + 1970) {
                elem.$_yearSelect[0].options[i].selected = true;
            }
        }
        elem.$_yearSelect[0].onchange = function (e) {
            var aclick = elem.find(".aboluo-aclick")[0];
            //重新赋值给变全局变量,所有的带状态的日期;然后下一步将创建table,完成动态样式,
            //这里要重读数据就5个位置,选择年时,上一个月,下一个月,设置节假日button,返回今天button
            createTabledate(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].value);
            if (!aclick) {
                //说明没选,或选的当天,算出选的这个月有多少天,与原来的那个月的天数一对比,如果原来的天数大于现在的天数,那么对换
                //这里先算当前月当前天,然后算出选择的那个月总天数,然后对比,如果当前天大于选择的那个月那天,对换
                var pervdays1 = getCurrMonthLashDay(elem.$_yearSelect[0].value, elem.$_monthSelect[0].value);
                if (new Date().getDate() > pervdays1) {
                    setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].value, pervdays1);
                } else {
                    setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].value, new Date().getDate());
                }
            } else {
                var adate = aclick.getAttribute("date");
                var aarr = adate.split("-");
                aarr[0] = parseInt(aarr[0]);
                aarr[1] = parseInt(aarr[1]);
                aarr[2] = parseInt(aarr[2]);
                var pervdays = getCurrMonthLashDay(elem.$_yearSelect[0].value, elem.$_monthSelect[0].value);
                if (aarr[2] > pervdays) {
                    aarr[2] = pervdays;
                }
                setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].value, aarr[2]);
            }
        };
    };

    //创建月的下拉框，并赋值,且添加事件，选择则对应的table日期也将改变,且已选中日期会跳到当前选择月的日期，然后给右边明细栏赋值
    function createMonthSelect(elem) {
        elem.$_monthSelect = $('<select name="aboluo-selectmonth" id="aboluo-selectmonth"></select>');
        elem.$_monthSelect[0].onchange = function (e) {
            var aclick = elem.find(".aboluo-aclick")[0];
            createTabledate(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value);
            if (!aclick) {
                //说明没选,或选的当天,算出选的这个月有多少天,与原来的那个月的天数一对比,如果原来的天数大于现在的天数,那么对换
                //这里先算当前月当前天,然后算出选择的那个月总天数,然后对比,如果当前天大于选择的那个月那天,对换
                var pervdays1 = getCurrMonthLashDay(elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value);
                if (new Date().getDate() > pervdays1) {
                    setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value, pervdays1);
                } else {
                    setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value, new Date().getDate());
                }
            } else {
                var adate = aclick.getAttribute("date");
                var aarr = adate.split("-");
                aarr[0] = parseInt(aarr[0]);
                aarr[1] = parseInt(aarr[1]);
                aarr[2] = parseInt(aarr[2]);
                var pervdays = getCurrMonthLashDay(elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value);
                if (aarr[2] > pervdays) {
                    aarr[2] = pervdays;
                }
                setRigth(elem, elem.$_yearSelect[0].value, elem.$_monthSelect[0].options[elem.$_monthSelect[0].selectedIndex].value, aarr[2]);
            }
        };
        var nowTime = new Date();
        var currMonth = nowTime.getMonth();
        for (var i = 0; i < 12; i++) {
            elem.$_monthSelect[0].options.add(new Option((i + 1) + "月", i + 1));
            if (currMonth == i) {
                elem.$_monthSelect[0].options[i].selected = true;
            }
        }
        elem.$_calendarmonth[0].insertBefore(elem.$_monthSelect[0], elem.$_anext[0]);
    };

    //根据传入的年月，创建对应的table日期,并且在每个td中创建a标签用于事件，与样式内边框的设置
    function createTabledate(elem, year, yue) {
        var rilitbodydele = elem.$_rilitbody;
        if (rilitbodydele != "" && rilitbodydele != null && rilitbodydele != 'undefined') {
            rilitbodydele.parentNode.removeChild(rilitbodydele);
        }
        var rilitbody = document.createElement('tbody');
        rilitbody.setAttribute("class", "aboluo-rilitbody");
        elem.$_rilitbody = rilitbody;
        elem.$_rilitable.append(rilitbody);
        //先得到当前月第一天是星期几,然后根据这个星期算前面几天的上个月最后几天.
        var date = setdateinfo(year, yue, 1);
        var weekday = date.getDay();
        var pervLastDay;
        if (weekday != 0) {
            pervLastDay = weekday - 1;
        } else {
            pervLastDay = weekday + 6;
        }
        //得到上个月最后一天;
        var pervMonthlastDay = getPervMonthLastDay(year, yue);
        //上月最后几天循环
        var lastdays = pervMonthlastDay - pervLastDay + 1;
        var tr = document.createElement('tr');
        tr.style.borderBottom = "1px solid #e3e4e6";
        for (var i = lastdays; i <= pervMonthlastDay; i++) {
            var td = document.createElement("td");
            var a = getA(parseInt(yue) - 1 == 0 ? parseInt(year) - 1 : year, parseInt(yue) - 1 == 0 ? 12 : parseInt(yue) - 1, i, true);
            a.style.color = "#BFBFC5";
            // a.href = 'javascript:pervA(' + parseInt(yue) - 1 == 0 ? parseInt(year) - 1 : year + ',' + parseInt(yue) - 1 == 0 ? 12 : parseInt(yue) - 1 + ',' + i + ');';
            td.appendChild(a);
            td.setAttribute("class", "aboluo-pervMonthDays");
            tr.appendChild(td);
        }
        //这个月开始的循环
        var startDays = 8 - weekday == 8 ? 1 : 8 - weekday;
        for (var i = 1; i <= startDays; i++) {
            var td = document.createElement("td");
            var b = getA(year, yue, i);
            td.appendChild(b);
            tr.appendChild(td);
        }
        rilitbody.appendChild(tr);
        //指定年月最后一天
        var currMonthLashDay = getCurrMonthLashDay(year, yue);
        //当月除开第一行的起点
        var currmonthStartDay = currMonthLashDay - (currMonthLashDay - startDays) + 1;
        //当月还剩余的天数
        var syts = currMonthLashDay - startDays;
        //循环次数
        var xhcs = 0;
        if (check(syts / 7)) {
            //是小数
            xhcs = Math.ceil(syts / 7);//向上取整
        } else {
            xhcs = syts / 7;
        }

        //这是下个月开始的变量;
        var jilvn = 1;
        for (var i = 0; i < xhcs; i++) {
            var tr1 = document.createElement('tr');
            if (i != xhcs - 1) {
                tr1.style.borderBottom = "1px solid #e3e4e6";
            }
            for (var n = 1; n <= 7; n++) {
                var td = document.createElement('td');
                if (startDays == 0) {
                    var c = getA(parseInt(yue) + 1 == parseInt(13) ? parseInt(year) + 1 : year, parseInt(yue) + 1 == parseInt(13) ? 1 : parseInt(yue) + 1, jilvn, true);
                    c.style.color = "#BFBFC5";
                    td.appendChild(c);
                    td.setAttribute("class", "aboluo-nextMonthDays");
                    jilvn++;
                    tr1.appendChild(td);
                    continue;
                } else {
                    startDays++;
                    var d = getA(year, yue, startDays);
                    td.appendChild(d);
                    if (startDays == currMonthLashDay) {
                        startDays = 0;
                    }
                    tr1.appendChild(td);
                }

            }
            rilitbody.appendChild(tr1);
        }
        setHolidayred(elem);//设置星期六星期天的样式
        setTrHeight(elem);//设置table日期的行高
        setA(elem); //设置td中a的事件
    };

    //创建date对象并赋值
    function setdateinfo(year, yue, day) {
        var date = new Date();
        date.setFullYear(parseInt(year));
        date.setMonth(parseInt(yue) - 1);
        date.setDate(parseInt(day));
        return date;
    };

    //得到指定月的上个月最后一天传进来按 12月算
    function getPervMonthLastDay(year, yue) {
        //当月就是  yue-1 也就是计算机里面的0-11月份,那么算上个月的最后一天就是当月的0天
        return parseInt(new Date(year, yue - 1, 0).getDate());
    };

    //创建a标签并设置公用属性
    function getA(year, yue, day, notMonth) {
        var a = document.createElement("a");
        a.href = "javascript:;";
        if (notMonth) {
            a.innerHTML = day;
        } else {
            if (!isweekend(year, yue, day)) {
                a.setAttribute('dateType', '10');
                a.innerHTML = day + '<span class="aboluo-td-a-ban">班</span>';
            } else {
                a.setAttribute('dateType', '20');
                a.innerHTML = day + '<span class="aboluo-td-a-xiu">休</span>';
            }
        }
        a.style.textDecoration = "none";
        a.setAttribute("date", year + "-" + yue + "-" + day);
        return a;
    };

    //得到指定月最后一天 传进来按 12月算
    function getCurrMonthLashDay(year, yue) {
        if (yue >= 12) {
            year = year + 1;
            yue = yue - 12;
        }
        return parseInt(new Date(year, yue, 0).getDate());
    };

    //判断c是否是小数
    function check(c) {
        var r = /^[+-]?[1-9]?[0-9]*\.[0-9]*$/;
        return r.test(c);
    };

    //遍历table将date事件等
    function setHolidayred(elem) {
        var rows = elem.$_rilitbody.rows;
        for (var i = 0; i < rows.length; i++) {
            for (var j = 0; j < rows[i].cells.length; j++) {
                var cell = rows[i].cells[j];
                var a = rows[i].cells[j].childNodes[0];
                var adate = a.getAttribute("date");
                var arr = adate.split("-");
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth();
                var day = date.getDate();
                if (arr[0] == year && arr[1] == month + 1 && arr[2] == day) {
                    cell.setAttribute("class", "aboluo-tdcurrToday");
                    a.setAttribute("class", "aboluo-currToday");
                }
                if (j >= rows[i].cells.length - 2) {
                    if (cell.getAttribute("class") != "aboluo-nextMonthDays" && cell.getAttribute("class") != "aboluo-pervMonthDays") {
                        a.style.color = "red";
                    }
                }
            }
        }
    };

    //动态设置tr高度,动态给td中的A设置高度与宽度
    function setTrHeight(elem) {
        var table = elem.$_rilidiv[0];
        var thead = elem.$_rilithead[0];
        var tbody = elem.$_rilitbody;
        var tbodyheight = table.offsetHeight - thead.offsetHeight;
        var rows = tbody.getElementsByTagName('tr');
        for (var i = 0; i < rows.length; i++) {
            rows[i].style.height = (tbodyheight / rows.length - 2) + "px";
            var tds = rows[i].getElementsByTagName("td");
            for (var j = 0; j < tds.length; j++) {
                var a = tds[j].childNodes[0];
                a.style.width = (tds[j].offsetWidth - 10) + "px";
                a.style.height = (tds[j].offsetHeight - 7) + "px";
                a.style.lineHeight = (tds[j].offsetHeight - 7) + "px";
            }
        }
    };

    //给tbody中的td中的A设置事件，上个月的天数,这个月的天数,下个月的天数三种对应的事件
    //这里还有个功能就是判断当前的A中日期是不是数据库中有带状态的日期,如果是就给相当的样式
    function setA(elem) {
        var tbody = elem.$_rilitbody;
        var opts = $.data(elem[0], "dxnCalendar").options;
        var holidays = opts.onGetHolidays.call(elem[0], parseInt(elem.$_yearSelect[0].value), parseInt(elem.$_monthSelect[0].value));
        if (!holidays) holidays = [];
        var arr = tbody.getElementsByTagName("a");
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].parentNode.className == "aboluo-pervMonthDays") {
                arr[i].onclick = null;
                arr[i].onclick = function () {
                    var date = this.getAttribute("date");
                    var datearr = date.split("-");
                    pervA(elem, datearr[0], datearr[1], datearr[2], this);
                    stopBubble(this);
                };
            } else if (arr[i].parentNode.className == "aboluo-nextMonthDays") {
                arr[i].onclick = null;
                arr[i].onclick = function () {
                    var date = this.getAttribute("date");
                    var datearr = date.split("-");
                    nextA(elem, datearr[0], datearr[1], datearr[2], this);
                    stopBubble(this);
                };
            } else {
                arr[i].onclick = null;
                arr[i].onclick = function () {
                    var date = this.getAttribute("date");
                    var datearr = date.split("-");
                    setRigth(elem, datearr[0], datearr[1], datearr[2], this);
                    stopBubble(this);
                };
            }
            for (var n = 0; n < holidays.length; n++) {
                var date = arr[i].getAttribute("date");
                if (formatByDate(holidays[n].dateTime) == formatByDate(date)) {
                    var span = $(arr[i]).find('span')[0]
                    if (holidays[n].dateType == 20) { //20休息
                        if (span) {
                            span.className = 'aboluo-td-a-xiu';
                            span.innerHTML = "休";
                        }
                        arr[i].setAttribute("dateTime", holidays[n].dateTime);
                        arr[i].setAttribute("dateType", holidays[n].dateType);
                    } else if (holidays[n].dateType == 30) { //30节假日
                        if (span) {
                            span.className = 'aboluo-td-a-jie';
                            span.innerHTML = "节";
                        }
                        arr[i].setAttribute("dateTime", holidays[n].dateTime);
                        arr[i].setAttribute("dateType", holidays[n].dateType);
                        // arr[i].style.background = "#fff0f0";
                    } else if (holidays[n].dateType == 10) { // 这里为了保证操作过的节假日的唯一性,不给样式只设置a的ztid
                        if (span) {
                            span.className = 'aboluo-td-a-ban';
                            span.innerHTML = "班";
                        }
                        arr[i].setAttribute("dateTime", holidays[n].dateTime);
                        arr[i].setAttribute("dateType", holidays[n].dateType);
                    }
                }
            }
        }
    };

    //给左右的a添加事件
    function leftrightclick(elem) {
        var lefta = elem.$_aperv[0];
        var righta = elem.$_anext[0];
        righta.onclick = function () {
            var monthselect = elem.$_monthSelect[0];
            var monthvalue = parseInt(monthselect.value);
            var yearselect = elem.$_yearSelect[0];
            var yearvalue = parseInt(yearselect.value)
            if (monthvalue == 12) {
                yearvalue += 1;
                monthvalue = 1;
            } else {
                monthvalue += 1;
            }
            monthselect.value = monthvalue;
            yearselect.value = yearvalue;
            var aclick = elem.find(".aboluo-aclick")[0];
            createTabledate(elem, yearselect.value, monthselect.value);

            //如果没有找到这个class说明没有点击或是点击的当天
            if (!aclick) {
                var pervdays1 = getCurrMonthLashDay(yearselect.value, monthselect.value + 1);
                if (new Date().getDate() > pervdays1) {
                    setRigth(elem, yearselect.value, monthselect.value, pervdays1);
                } else {
                    setRigth(elem, yearselect.value, monthselect.value, new Date().getDate());
                }
            } else {
                var adate = aclick.getAttribute("date");
                var aarr = adate.split("-");
                aarr[0] = parseInt(aarr[0]);
                aarr[1] = parseInt(aarr[1]);
                aarr[2] = parseInt(aarr[2]);
                var pervdays = getCurrMonthLashDay(aarr[0], aarr[1] + 1);
                if (aarr[2] > pervdays) {
                    aarr[2] = pervdays;
                }
                setRigth(elem, aarr[1] + 1 == 13 ? aarr[0] + 1 : aarr[0], aarr[1] + 1 == 13 ? 1 : aarr[1] + 1, aarr[2]);
            }
        }
        lefta.onclick = function () {
            var monthselect = elem.$_monthSelect[0];
            var monthvalue = parseInt(monthselect.value);
            var yearselect = elem.$_yearSelect[0];
            var yearvalue = parseInt(yearselect.value)
            if (monthvalue == 1) {
                yearvalue -= 1;
                monthvalue = 12;
            } else {
                monthvalue -= 1;
            }
            monthselect.value = monthvalue;
            yearselect.value = yearvalue;
            var aclick = elem.find(".aboluo-aclick")[0];
            createTabledate(elem, yearselect.value, monthselect.value);
            //如果没有找到这个class说明没有点击或是点击的当天
            if (!aclick) {
                //这个时候向上一个月,那么
                var pervdays1 = getPervMonthLastDay(yearselect.value, monthselect.value);
                if (new Date().getDate() > pervdays1) {
                    setRigth(elem, yearselect.value, monthselect.value, pervdays1);
                } else {
                    setRigth(elem, yearselect.value, monthselect.value, new Date().getDate());
                }
            } else {
                var adate = aclick.getAttribute("date");
                var aarr = adate.split("-");
                aarr[0] = parseInt(aarr[0]);
                aarr[1] = parseInt(aarr[1]);
                aarr[2] = parseInt(aarr[2]);
                var pervdays = getPervMonthLastDay(aarr[0], aarr[1]);
                if (aarr[2] > pervdays) {
                    aarr[2] = pervdays;
                }
                setRigth(elem, aarr[1] - 1 == 0 ? aarr[0] - 1 : aarr[0], aarr[1] - 1 == 0 ? 12 : aarr[1] - 1, aarr[2]);
            }
        }

        var today = elem.$_inputToToday[0];
        today.onclick = null;
        today.onclick = function () {
            var monthselect = elem.$_monthSelect[0];
            var yearselect = elem.$_yearSelect[0];
            var date = new Date();
            monthselect.value = date.getMonth() + 1;
            yearselect.value = date.getFullYear();
            createTabledate(elem, yearselect.value, monthselect.value);
            setRigth(elem, date.getFullYear(), date.getMonth() + 1, date.getDate());
        };
    };

    //给rightdiv创建元素并赋值，根据传入的年月日给内部的元素赋值 ,月份是 1-12
    function setRigth(elem, year, yue, day) {
        //先清空
        elem.$_xssj.html("");
        elem.$_ssjjr.html("");
        year = year.toString();
        yue = yue.toString();
        day = day.toString();
        //设置rigthdiv的marginleft;
        var rigthdiv = elem.$_rightdiv[0];
        var w = elem[0];
        rigthdiv.style.marginLeft = (w.offsetWidth * 0.7 + 4) + "px";  //设置margin-left
        //给p中添加span显示值
        var span = document.createElement('span');
        var date = setdateinfo(year, yue, day);
        span.innerHTML = formatByYearyueday(year, yue, day);
        var span1 = document.createElement('span');
        var week = getWeek(date.getDay());
        span1.innerHTML = week;
        var aboluoxssj = elem.$_xssj[0];
        aboluoxssj.appendChild(span);
        aboluoxssj.appendChild(span1);
        var currday = elem.$_currday[0];
        currday.innerHTML = day;
        currday.style.lineHeight = currday.offsetHeight + "px";    //实际在得到长宽时不能用style.height，得用.offsetHeight,但是设置的时候要用style.height=...
        var szrq = elem.$_ssjjr[0];
        szrq.style.marginTop = "20px";
        var span2 = document.createElement('span');
        span2.innerHTML = "设置日志状态:";
        szrq.appendChild(span2);
        var szrqselect = document.createElement("select");
        szrqselect.style.width = (elem.$_rightdiv[0].offsetWidth * 0.9) + "px";
        szrqselect.options.add(new Option("无", "0")); //0代表还原
        //这里要判断一下如果是星期67就只能设置上班,如果是星期1-5就只能设置休息
        var aHtml = elem.find("[date='" + year + "-" + yue + "-" + day + "']")[0];
        var datetype = aHtml.getAttribute('datetype');
        if (datetype == 10) {
            szrqselect.options.add(new Option("休息", '20'));
            szrqselect.options.add(new Option("节假", '30'));
        } else {
            szrqselect.options.add(new Option("上班", "10"));
        }
        szrq.appendChild(szrqselect);
        var szrqbutton = document.createElement('input');
        szrqbutton.type = "button";
        szrqbutton.className = "btn";  //设置class
        szrqbutton.value = "确认";
        szrqbutton.onclick = function () {
            var opts = $.data(elem[0], "dxnCalendar").options;
            if (szrqselect.value != 0 && opts.setHolidayUrl) {
                var dataValue = year + '-' + ('0' + yue).slice(-2) + '-' + ('0' + day).slice(-2);
                if (dataValue) dataValue = dataValue.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
                $.ajax({
                    type: "POST",
                    url: opts.setHolidayUrl,
                    data: {"date": dataValue, "dateType": szrqselect.value}, //这里用ajax可以改变状态
                    async: false,
                    success: function () {
                        createTabledate(elem, year, yue);
                        setRigth(elem, year, yue, day);
                    },
                    error: function (json) {
                        opts.onsetHolidayUrlError.call(elem[0], json);
                        // alert('设置失败！')
                    }
                });
            }
        };
        szrq.appendChild(szrqbutton);
        setaclass(elem, year, yue, day);
    };

    //根据年月日判断是不是星期六星期天 //yue 按12算
    function isweekend(year, yue, day) {
        var date = new Date();
        date.setFullYear(year);
        date.setMonth(yue - 1);
        date.setDate(day);
        var week = date.getDay();
        if (week == 0 || week == 6) {
            return true;
        }
        return false;
    };

    function formatByYearyueday(year, yue, day) {
        year = year.toString();
        yue = yue.toString();
        day = day.toString();
        return year + "-" + (yue.length < 2 ? '0' + yue : yue) + "-" + (day.length < 2 ? '0' + day : day);
    };

    //根据getDay()返回对应的星期字符串
    function getWeek(val) {
        var weekxq = new Array();
        weekxq[0] = "星期日";
        weekxq[1] = "星期一";
        weekxq[2] = "星期二";
        weekxq[3] = "星期三";
        weekxq[4] = "星期四";
        weekxq[5] = "星期五";
        weekxq[6] = "星期六";
        return weekxq[val];
    };

    //a点击选中样式,先清除再设置选中样式
    function setaclass(elem, year, yue, day) {
        var a = elem.find(".aboluo-aclick")[0];
        if (a) a.className = "";
        var date = new Date();
        var year1 = date.getFullYear();
        var month1 = date.getMonth();
        var day1 = date.getDate();
        if (!(year1 == year && yue == month1 + 1 && day1 == day)) {
            var tbody = elem.$_rilitbody;
            var arr = tbody.getElementsByTagName("a");
            for (var i = 0; i < arr.length; i++) {
                var date = arr[i].getAttribute("date");
                var datearr = date.split("-");
                if (datearr[0] == year && datearr[1] == yue && datearr[2] == day) {
                    arr[i].setAttribute("class", "aboluo-aclick");
                }
            }
        }
    };

    function formatByDate(date) {
        date = date.substring(0, 10);
        var daxx = date.toString().split("-");
        return daxx[0] + "-" + (daxx[1].length < 2 ? '0' + daxx[1] : daxx[1]) + "-" + (daxx[2].length < 2 ? '0' + daxx[2] : daxx[2]);
    };

    //给上一个月最后几天点击跳转月份
    function pervA(elem, year, yue, day) {
        createTabledate(elem, year, yue);  //创建对应的table(日期)
        setRigth(elem, year, yue, day);    //设置右边明细栏内容
        updateSelect(elem, year, yue);    //改变年月select值
    };

    //给上一个月最后几天点击跳转月份
    function nextA(elem, year, yue, day) {
        createTabledate(elem, year, yue);
        setRigth(elem, year, yue, day);
        updateSelect(elem, year, yue);
    };

    function updateSelect(elem, year, yue) {
        var selectmonth = elem.$_monthSelect[0];
        var selectyear = elem.$_yearSelect[0];
        if (selectmonth) selectmonth.value = yue;
        if (selectyear) selectyear.value = year;
    };

    //阻止冒泡
    function stopBubble(e) {
        if (e && e.stopPropagation) {// 别的浏览器
            e.stopPropagation();
        } else { //IE
            window.event.cancelBubble = true;
        }
    };
})(jQuery)
