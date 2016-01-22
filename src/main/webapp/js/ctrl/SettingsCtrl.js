app
    .controller('SettingsCtrl',['$scope','QuoteSvc','TagSvc', function($scope,QuoteSvc,TagSvc){
	$scope.downloaded = 0;
	$scope.$parent.show = true;
	$scope.exportProgress = 0;
	
	$scope.export=function(){
	    $scope.$parent.click();
	    $scope.exportProgress = 0;
	    QuoteSvc.count().success(function(count){
		var exported = [];
		var total = parseInt(count);
		var page = 10;
		var load = function(o) {
		    $scope.exportProgress = parseInt( o*100 / total );
		    if(o >= total)
			Base64.download(JSON.stringify(exported),'quotes.json');
		    else
			QuoteSvc.list(o,Math.min(total-o,page)).success(function(response){
			    angular.forEach(response,function(quote,i){
				TagSvc.findByQuoteId(quote.key).success(function(tags){
				    quote.tags=tags;
				    exported.push(quote);

				})
				.finally(function(){
				    if(i === response.length-1) load(o+page);
				});
				
			    });
			})
		};
		load(0);
	    });
	    
	};
	
	$scope.exportCSV=function(){
	    $scope.$parent.click();
	    $scope.exportProgress = 0;
	    QuoteSvc.count().success(function(count){
		var exported = 'quote , personId , videoId , start , end , tags , youtube \n';
		var total = parseInt(count);
		var page = 10;
		var load = function(o) {
		    $scope.exportProgress = parseInt( o*100 / total );
		    if(o >= total)
			Base64.download(exported,'quotes.csv');
		    else
			QuoteSvc.list(o,Math.min(total-o,page)).success(function(response){
			    angular.forEach(response,function(quote,i){
				
				TagSvc.findByQuoteId(quote.key).success(function(tags){
				    
				    var tmp = '';
				    angular.forEach(tags,function(tag){
					tmp += ' #'+ tag.tag;
				    });
				    
				    exported += ''+quote.quote+' , '
					    + quote.personId +' , '
					    +quote.videoId+' , '
					    +parseInt(quote.start)+' , '
					    +parseInt(quote.end)+' , '
					    +tmp+' , '
					    +'https://youtu.be/'+quote.videoId+'?t='+parseInt(quote.start)+'s \n';

				})
				.finally(function(){
				    if(i === response.length-1) load(o+page);
				});
				
			    });
		    })
		};
		load(0);
	    });
	    
	};
	
    }]);