TLDR; This article takes a look of how to approach a common problem, that usually gets’ solved with parent/child or recursive relationships by taking a look at a different solution by using a functionality called the path hierarchy tokenizer within Elasticsearch.

/home/alr/devel/spinscale.de/website
/home/alr/devel/spinscale.de/website/index.html
/tmp/trash.xml

Electronics > White goods > Refrigerators > Fridge/Freezer Combination
The second use-case comes from my prime example from my former job, an e-commerce product search engine. Each product could be in multiple categories, that were modeled as breadcrumbs.

You may have stumbled over the join field type already. T
hat field type in combination with the has_parent query, 
the has_child query, the children aggregation and the inner hits functionality 
allows to create a single 1:n or parent/child relationship within your data. 
Each parent and child are separate documents. 
This is sometimes considered as a solution for the above use-case, 
as this allows to create a hierarchy - which might be a good idea for the breadcrumbs above. 
Have a hierarchy Electronics, that contains all the White goods referenced to it.
This comes with a couple of advantages and disadvantages:

Single document updates are easy
Moving a category to another is easy, a single document update with a new parent is sufficient
Powerful query capabilities to execute full text search against parent and children in the same query
To walk up the hierarchy, you would need as many parent queries, as you have depth
Every document can only have a single parent
Everything would need to be stored on a single shard

https://spinscale.de/posts/2021-03-17-search-hierarchies-using-elasticsearch-path-hierarchy-tokenizer.html
The main component to make everything work, is the `path hierarchy tokenizer`. Let’s get up and running with a proper analyze example first

https://www.elastic.co/guide/en/elasticsearch/reference/7.11/parent-join.html#parent-join
Parent-join and performance
The join field shouldn’t be used like joins in a relation database. In Elasticsearch the key to good performance is to de-normalize your data into documents. Each join field, has_child or has_parent query adds a significant tax to your query performance. It can also trigger global ordinals to be built.

The only case where the join field makes sense is if your data contains a one-to-many relationship where one entity significantly outnumbers the other entity. An example of such case is a use case with products and offers for these products. In the case that offers significantly outnumbers the number of products then it makes sense to model the product as parent document and the offer as child document.

